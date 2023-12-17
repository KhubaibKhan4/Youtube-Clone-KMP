package org.company.app

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import org.jetbrains.skia.Bitmap
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.base.State
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat
import java.lang.Float.min
import java.nio.ByteBuffer

interface VideoPlayerState {
    val time: Long //Timestamp milliseconds
    val progress: Float //from 0 to 1 -> 0 is start and 1 is end
    val aspectRatio: Float
    fun seek(position: Float)
    fun togglePause()

}

class VlcjVideoPlayerState: VideoPlayerState {
    internal val internalState = RenderState()
    override var time: Long = 0L
        internal set
    override var progress: Float by mutableStateOf(0f)
        internal set
    val mediaPlayer: MediaPlayer get() = internalState.mediaPlayerComponent.mediaPlayer()
    override val aspectRatio: Float get() = internalState.aspectRatio

    override fun seek(position: Float) {
        mediaPlayer.controls().setPosition(position)
    }

    override fun togglePause() {
        val state = mediaPlayer.media().info().state()
        if (state == State.PLAYING) {
            mediaPlayer.controls().pause()
        } else {
            mediaPlayer.controls().play()
        }
    }

    init {
        println("New VideoPlayerState")
    }
}

private var frameCounter = 0L
private var lastTs = 0L


@Composable
fun VideoPlayerDirect(
    modifier: Modifier = Modifier,
    state: VlcjVideoPlayerState = remember { VlcjVideoPlayerState() },
    url: String
) {
    NativeDiscovery().discover()

    DisposableEffect(state) {
        val eventListener = object : MediaPlayerEventAdapter() {
            override fun playing(mediaPlayer: MediaPlayer) {
                println("Playing: ${mediaPlayer.media().info().textTracks()}")
            }
            override fun timeChanged(mediaPlayer: MediaPlayer, newTime: Long) {
                state.time = newTime
            }
            override fun positionChanged(mediaPlayer: MediaPlayer, newPosition: Float) {
                //println("Position: $newPosition")
                state.progress = newPosition
            }
        }
        state.mediaPlayer.events().addMediaPlayerEventListener(eventListener)
        onDispose {
            state.mediaPlayer.events().removeMediaPlayerEventListener(eventListener)
            state.mediaPlayer.release()
        }
    }
    var frameTime: Long by remember { mutableStateOf(0L) }
    LaunchedEffect(url) {
        println("Sideffect started")
        state.mediaPlayer.media()?.start(url)
        state.mediaPlayer.subpictures().setTrack(-1)
        while(true) {
            withFrameMillis {
                frameTime = it
            }
        }
    }
    return Canvas(modifier) {
        state.internalState.updateComposeImage(frameTime)?.let { image ->
            val scaleW = size.width / image.width.toFloat()
            val scaleH = size.height / image.height.toFloat()
            //val frame = renderState.frame // needed to enforce high fps redrawing
            //val fts = frameTime
            scale(scale = min(scaleH, scaleW), pivot = Offset.Zero) {
                drawImage(image)
            }
        }
        frameCounter++
        if (frameCounter % 60L == 0L) {
            val time = System.currentTimeMillis()
            val delta = time - lastTs
            println("FPS: ${1000 / (delta / 60)}")
            lastTs = time
        }
    }
}

internal class RenderState {
    var aspectRatio: Float by mutableStateOf(1f)
        private set

    var currentBuffer: ByteBuffer? = null

    private var buffer: ByteArray = ByteArray(0)
    private var bufferBitmap: Bitmap = Bitmap()
    private var composeImage: ImageBitmap? = null

    init {
        println("New RenderState")
    }

    fun updateComposeImage(frameTime: Long): ImageBitmap? {
        currentBuffer?.let {
            it.get(buffer)
            it.rewind()
            bufferBitmap.installPixels(buffer)
            return composeImage
        }
        return null
    }

    private val bufferFormatCallback = object : BufferFormatCallback {
        override fun getBufferFormat(sourceWidth: Int, sourceHeight: Int): BufferFormat {
            println("Size: $sourceWidth x $sourceHeight")
            bufferBitmap = Bitmap().also {
                it.allocN32Pixels(sourceWidth, sourceHeight, true)
            }
            composeImage = bufferBitmap.asComposeImageBitmap()
            buffer = ByteArray(sourceWidth * sourceHeight * 4)
            //renderCallback.setBuffer(IntArray(sourceWidth * sourceHeight * 4))
            with(bufferBitmap) {
                aspectRatio = if (width <= 0 || height <= 0) 1f else width.toFloat() / height.toFloat()
                println("Set aspectRatio to: $aspectRatio")
            }
            return RV32BufferFormat(sourceWidth, sourceHeight)
        }
        override fun allocatedBuffers(buffers: Array<out ByteBuffer>) {
            currentBuffer = buffers.firstOrNull()
        }
    }
    private val renderCallback = RenderCallback { _, nativeBuffers, _ ->
        //currentBuffer = nativeBuffers.first()
    }
    val mediaPlayerComponent = CallbackMediaPlayerComponent(
        null,
        null,
        null,
        true,
        null,
        renderCallback,
        bufferFormatCallback,
        null
    )
}
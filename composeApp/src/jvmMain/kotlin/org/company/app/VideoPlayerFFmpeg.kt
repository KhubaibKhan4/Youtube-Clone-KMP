/*
package org.company.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


class FFmpegVideoPlayerState {
    private val kContext = KAVFormatContext()

    var time: Long = 0L
        internal set
    var progress: Float by mutableStateOf(0f)
        internal set
    var aspectRatio: Float by mutableStateOf(1f)
        private set
    var displayFPS: Float by mutableStateOf(0f)
        internal set
    var decodedFPS: Float by mutableStateOf(0f)
        internal set

    var frameGrabber: KFrameGrabber? by mutableStateOf(null)
        private set

    var metadata: Map<String, String> by mutableStateOf(emptyMap())
        private set

    fun open(file: String) {
        kContext.openInput(file)
        metadata = kContext.findMetadata()
    }

    fun close() {
        kContext.closeInput()
    }

    fun streams(): List<KVideoStream> = kContext.findVideoStreams()
    fun codec(stream: KVideoStream): KAVCodec = kContext.findCodec(stream)

    fun play(
        stream: KVideoStream,
        hwDecoder: KHWDecoder? = null,
        targetSize: IntSize? = null
    ) {
        frameGrabber?.close() // close running frame grabber
        frameGrabber = KFrameGrabber(stream, kContext, hwDecoder, targetSize)
        aspectRatio = stream.width.toFloat() / stream.height.toFloat()
    }

    fun stop() {
        frameGrabber?.close()
        frameGrabber = null
    }

    internal var seekPosition: Float = -1f

    fun seek(position: Float) {
        seekPosition = position
    }

    fun togglePause() {
        //TODO
    }

    init {
        println("New VideoPlayerState")
    }
}

@Composable
fun VideoPlayerFFMpeg(
    modifier: Modifier = Modifier,
    state: FFmpegVideoPlayerState = remember { FFmpegVideoPlayerState() },
    file: String
) {
    var frame by remember { mutableStateOf(0) }
    val videoImage = remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(file, Dispatchers.IO) {
        state.open(file)
        val stream = state.streams().first()
        val codec = state.codec(stream)
        val scale = 1
        val targetSize = IntSize(stream.width / scale, stream.height / scale)
        //state.play(stream, null) // No hw accel
        state.play(stream, codec.hwDecoder.firstOrNull(), targetSize) // Hw accel
        val frameGrabber = requireNotNull(state.frameGrabber) { "Frame grabber not initialized!" }
        var startTs = -1L
        var lastDisplayFrameCount = 0L
        var lastDecodedFrameCount = 0L
        var lastTs = -1L
        while (isActive) {
            withFrameMillis { currentTs ->
                if (startTs < 0) {
                    startTs = currentTs
                    lastTs = currentTs
                }
                if (state.seekPosition >= 0) {
                    //Set new start time
                    val seekMillis: Long = (stream.durationMillis * state.seekPosition.toDouble()).toLong()
                    startTs = currentTs - seekMillis
                    state.seekPosition = -1f
                }
                val pos = currentTs - startTs
                //println("Ts millis: $pos")
                frameGrabber.grabNextFrame(pos)
                videoImage.value = frameGrabber.composeImage
                state.time = pos
                state.progress = (pos.toDouble() / stream.durationMillis.toDouble()).toFloat()
                frame++
                if (frame % 60 == 0) {
                    val time = (currentTs - lastTs).toFloat() / 1000f

                    val newDisplayFrameCount = frameGrabber.bitmapFrameCounter
                    val displayFrameCount = newDisplayFrameCount - lastDisplayFrameCount
                    state.displayFPS = displayFrameCount.toFloat() / time

                    val newDecodedFrameCount = frameGrabber.decodedFrameCounter
                    val decodedFrameCount = newDecodedFrameCount - lastDecodedFrameCount
                    state.decodedFPS = decodedFrameCount.toFloat() / time

                    lastDisplayFrameCount = newDisplayFrameCount
                    lastDecodedFrameCount = newDecodedFrameCount
                    lastTs = currentTs
                }
            }
            delay(5)
        }
        state.stop()
        state.close()
    }


    videoImage.value?.let { image ->
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)// This makes the Spacer take the full available size
                .background(color = Color.Black) // Optional: Set background color to black
                .drawBehind {
                    val scaleW = size.width / image.width.toFloat()
                    val scaleH = size.height / image.height.toFloat()

                    // Choose the desired scaling factor
                    val scale = kotlin.math.min(scaleH, scaleW)

                    // Center the video in the Spacer
                    val offsetX = (size.width - image.width * scale) / 2
                    val offsetY = (size.height - image.height * scale) / 2

                    // Apply the scale and translation
                    scale(scale, pivot = Offset.Zero) {
                        translate(offsetX, offsetY) {
                            drawImage(image)
                        }
                    }
                }
        )
    }

}*/

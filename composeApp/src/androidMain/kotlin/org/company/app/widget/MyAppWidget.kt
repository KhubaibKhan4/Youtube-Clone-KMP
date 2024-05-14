package org.company.app.widget

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.text.Text

class MyAppWidget : GlanceAppWidget() {
    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            SMALL_SQUARE,
            HORIZONTAL_RECTANGLE,
            BIG_SQUARE
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MyContent()
        }
    }


    @Composable
    fun MyContent() {
        val size = LocalSize.current
        Column {
            if (size.height >= BIG_SQUARE.height) {
                Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
            }
            Row(horizontalAlignment = androidx.glance.layout.Alignment.CenterHorizontally) {
                if (size.width >= HORIZONTAL_RECTANGLE.width) {
                    Button("School", onClick = {})
                }
            }
            if (size.height >= BIG_SQUARE.height) {
                Text(text = "provided by X")
            }
        }
    }
}
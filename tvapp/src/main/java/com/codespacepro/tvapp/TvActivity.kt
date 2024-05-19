package com.codespacepro.tvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.tv.material3.Text
import com.codespacepro.tvapp.ui.theme.YoutubeCloneTheme

class TvActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YoutubeCloneTheme {
                Text(text = "Hello World")
            }
        }
    }
}
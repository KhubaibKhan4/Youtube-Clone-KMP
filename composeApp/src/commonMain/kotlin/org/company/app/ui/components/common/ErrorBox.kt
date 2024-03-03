package org.company.app.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.company.app.theme.LocalThemeIsDark

@Composable
fun ErrorBox(error: String) {
    val isDark by LocalThemeIsDark.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SelectionContainer {
            Text(text = error,
                color = if (isDark) Color.White else Color.Black)
        }
    }
}
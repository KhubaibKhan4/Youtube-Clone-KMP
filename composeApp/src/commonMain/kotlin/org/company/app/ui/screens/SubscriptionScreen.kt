package org.company.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.company.app.ui.components.TopBar

class SubscriptionScreen : Screen {
    @Composable
    override fun Content() {
        TopBar(modifier = Modifier.fillMaxWidth().padding(top = 49.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Subscriptions,
                contentDescription = "Subscriptions",
                modifier = Modifier.size(140.dp),
                tint = Color.LightGray
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Don't miss new videos",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign in to see updates from favorite \nYouTube channels",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {},
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Sign in")
            }
        }
    }
}
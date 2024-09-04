package org.company.app.presentation.ui.screens.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding 
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cafe.adriel.voyager.core.screen.Screen
import org.company.app.presentation.ui.components.topappbar.TopBar
import org.company.app.provideShortCuts

class LibraryScreen : Screen {
    @Composable
    override fun Content() {
        
        var isShortEnabled by remember { mutableStateOf(false) }
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TopBar(modifier = Modifier.fillMaxWidth().padding(top = 10.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 30.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.LibraryAdd,
                    contentDescription = "Library",
                    modifier = Modifier.size(140.dp),
                    tint = Color.LightGray
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Enjoy your favorite videos",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Sign in to access videos that you've liked or \n saved",
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
                
                Spacer(modifier = Modifier.height(15.dp))

                TextButton(
                    onClick = {
                        isShortEnabled = !isShortEnabled
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Add Videos ShortCut")
                }
                
                if (isShortEnabled) {
                    provideShortCuts()
                }
            }
        }
    }
}

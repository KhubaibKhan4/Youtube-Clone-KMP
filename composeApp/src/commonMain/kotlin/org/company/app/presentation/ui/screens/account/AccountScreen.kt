package org.company.app.presentation.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row 
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState 
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpCenter
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.company.app.theme.LocalThemeIsDark
import org.jetbrains.compose.resources.painterResource
import youtube_clone.composeapp.generated.resources.Res
import youtube_clone.composeapp.generated.resources.do_more_with_youtube
import youtube_clone.composeapp.generated.resources.youtube_kids
import youtube_clone.composeapp.generated.resources.youtube_music

object AccountScreen : Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val isDark by LocalThemeIsDark.current
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp, start = 6.dp), 
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(
                    onClick = {
                        navigator?.pop()
                    },
                    modifier = Modifier.padding(top = 12.dp, start = 4.dp)
                        .align(alignment = Alignment.TopStart)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                }
            }
            
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(Res.drawable.do_more_with_youtube),
                    contentDescription = null,
                    modifier = Modifier.size(165.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Do more with YouTube",
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize
                )

                Text(
                    text = "Sign in now to upload, save, and comment on \n videos",
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {},
                    enabled = true,
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier.width(65.dp),
                ) {
                    Text(text = "SIGN IN")
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = DividerDefaults.Thickness,
                color = Color.LightGray
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Settings")
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.HelpCenter, contentDescription = null)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Help & feedback")
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = DividerDefaults.Thickness,
                color = Color.LightGray
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 6.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painterResource(Res.drawable.youtube_music), contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "YouTube Music")
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painterResource(Res.drawable.youtube_kids), contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "YouTube Kids")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
            }
        
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 10.sp,
                        color = if (isDark) Color.LightGray else Color.DarkGray
                    )
                    Text(text = ".")
                    Text(
                        text = "Terms of Service",
                        fontSize = 10.sp,
                        color = if (isDark) Color.LightGray else Color.DarkGray
                    )
                }
            }
        }
    }
    override val options: TabOptions
        @Composable
        get() {
            val title = "Account Screen"
            val icon = rememberVectorPainter(Icons.Default.AccountCircle)
            val index: UShort = 5u
            return TabOptions(
               index, title, icon
            )
        }
}

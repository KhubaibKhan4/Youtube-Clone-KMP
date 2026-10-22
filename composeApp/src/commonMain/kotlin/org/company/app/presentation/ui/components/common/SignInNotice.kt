package org.company.app.presentation.ui.components.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.company.app.openUrl

/**
 * Google account sign-in needs an OAuth client ID per platform, which this open-source build
 * doesn't ship. Rather than a dead button, explain that and offer YouTube in the browser.
 */
@Composable
fun SignInNotice(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = null) },
        title = { Text("Sign-in not available yet") },
        text = {
            Text(
                "This app browses YouTube with a public API key, so watching, searching and " +
                    "downloading work without an account. Signing in to your Google account " +
                    "isn't supported in this build yet."
            )
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Got it") } },
        dismissButton = {
            TextButton(onClick = {
                openUrl("https://accounts.google.com/ServiceLogin?service=youtube")
                onDismiss()
            }) { Text("Open YouTube") }
        }
    )
}

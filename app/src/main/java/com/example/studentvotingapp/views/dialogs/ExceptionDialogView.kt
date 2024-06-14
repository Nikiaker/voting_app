package com.example.studentvotingapp.views.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

class ExceptionDialogView {
    companion object {
        @Composable
        fun ExceptionDialog(exceptionMessage: String, onDismiss: () -> Unit) {
            AlertDialog(
                title = {
                    Text(text = "An exception has been thrown")
                },
                text = {
                    Text(text = exceptionMessage)
                },
                onDismissRequest = {
                    onDismiss()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            )
        }
    }
}
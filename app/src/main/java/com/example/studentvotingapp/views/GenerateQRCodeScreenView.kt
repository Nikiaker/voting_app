package com.example.studentvotingapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.studentvotingapp.logic.QRCode
import com.example.studentvotingapp.models.Voting

class GenerateQRCodeScreenView {
    companion object {
        @Composable
        fun GenerateQRCodeScreen(voting: Pair<String, Voting>) {
            var selectedCandidate by remember { mutableStateOf<String?>(null) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Generate QR Code for Voting: ${voting.second.title}", style = MaterialTheme.typography.h6, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))

                voting.second.candidates.forEach { candidate ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCandidate = candidate }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedCandidate == candidate,
                            onClick = { selectedCandidate = candidate }
                        )
                        Text(text = candidate, modifier = Modifier.padding(start = 8.dp), color = Color.White)
                    }
                }

                selectedCandidate?.let { candidate ->
                    val qrText = "${voting.first},$candidate"
                    val qrBitmap = QRCode.generateQRCode(qrText)

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}
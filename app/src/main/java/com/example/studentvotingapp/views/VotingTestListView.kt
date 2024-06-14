package com.example.studentvotingapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.VotingService
import com.example.studentvotingapp.views.dialogs.ExceptionDialogView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VotingTestListView {
    companion object {
        @Composable
        fun VotingsListScreen() {
            var votings by remember { mutableStateOf<Map<String, Voting>>(emptyMap()) }
            val votingService = VotingService()

            var isException by remember { mutableStateOf(false) }
            val exceptionCallback = { isException = false }
            var exceptionMessage by remember { mutableStateOf("") }
            if (isException) {
                ExceptionDialogView.ExceptionDialog(exceptionMessage = exceptionMessage, onDismiss = exceptionCallback)
            }

            LaunchedEffect(Unit) {
                votingService.getAllVotings(
                    { votings = it },
                    {
                        exceptionMessage = it.message.toString()
                        isException = true
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(votings.toList()) { _, voting ->
                    VotingItem(voting = voting.second, onDelete = {})
                }
            }
        }

        @Composable
        private fun VotingItem(voting: Voting, onDelete: () -> Unit) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { /* Handle voting item click */ },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Title: ${voting.title}")
                    Text(text = "Description: ${voting.description}")
                    Text(text = "Start Time: ${voting.startTime?.toDate()}")
                    Text(text = "End Time: ${voting.endTime?.toDate()}")
                    Text(text = "Candidates: ${voting.candidates.joinToString(", ")}")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
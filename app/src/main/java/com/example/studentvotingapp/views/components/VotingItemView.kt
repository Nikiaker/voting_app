package com.example.studentvotingapp.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentvotingapp.models.Voting

class VotingItemView {
    companion object {
        @Composable
        fun VotingItem(voting: Voting, onVote: () -> Unit) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .clickable { onVote() },
                shape = MaterialTheme.shapes.medium, // This applies the rounded corners
                elevation = 4.dp,
                backgroundColor = Color.Cyan
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Title: ${voting.title}")
                    Text(text = "Description: ${voting.description}")
                    Text(text = "Start Time: ${voting.startTime?.toDate()}")
                    Text(text = "End Time: ${voting.endTime?.toDate()}")
                    Text(text = "Candidates: ${voting.candidates.joinToString(", ")}")
                }
            }
        }
    }
}
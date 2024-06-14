package com.example.studentvotingapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentvotingapp.models.Vote
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.VotingService
import com.example.studentvotingapp.views.components.VotingItemView


class VotingResultsScreenView {
    companion object {
        @Composable
        fun VotingResultsScreen(voting: Voting, onBack: () -> Unit) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = voting.title, style = MaterialTheme.typography.h6, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = voting.description, color = Color.White)

                voting.votes.forEach { (candidate, voteCount) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "$candidate: $voteCount votes", color = Color.White, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}
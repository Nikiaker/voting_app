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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentvotingapp.models.Vote
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.VotingService
import com.example.studentvotingapp.views.components.VotingItemView

class FinishedVotesScreenView {
    companion object {
        @Composable
        fun FinishedVotesScreen(votingCallback: (Pair<String, Voting>) -> Unit) {
            var finishedVotes by remember { mutableStateOf<Map<String, Voting>>(emptyMap()) }

            LaunchedEffect(Unit) {
                val votingsService = VotingService()
                votingsService.getFinishedVotings { finishedVotes = it }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(finishedVotes.toList()) { _, voting ->
                    VotingItemView.VotingItem(voting = voting.second, onVote = {
                        votingCallback(voting)
                    })
                }
            }
        }
    }
}
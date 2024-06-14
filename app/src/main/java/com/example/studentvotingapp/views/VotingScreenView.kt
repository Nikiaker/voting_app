package com.example.studentvotingapp.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.studentvotingapp.GlobalUserData
import com.example.studentvotingapp.models.Vote
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.VoteService
import com.example.studentvotingapp.models.services.VotingService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VotingScreenView {
    companion object {
        @Composable
        fun VotingScreen(voting: Pair<String, Voting>, onVoteSubmitted: () -> Unit) {
            var selectedCandidate by remember { mutableStateOf<String?>(null) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = voting.second.title, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = voting.second.description)

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
                        Text(text = candidate, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedCandidate != null) {
                            saveVote(voting.first, selectedCandidate!!)
                            onVoteSubmitted()
                        } else {
                            //Toast.makeText(LocalContext.current, "Please select a candidate", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = selectedCandidate != null,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Vote")
                }
            }
        }

        private fun saveVote(votingId: String, candidate: String) {
            val votingService = VotingService()
            votingService.updateVotingCandidate(votingId, candidate)

            val voteService = VoteService()
            val vote = Vote(
                votingUid = votingId,
                userUid = GlobalUserData.user.uid
            )
            voteService.addVote(vote, {}, {})
        }

    }
}
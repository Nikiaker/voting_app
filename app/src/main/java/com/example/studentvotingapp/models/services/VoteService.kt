package com.example.studentvotingapp.models.services

import com.example.studentvotingapp.GlobalUserData
import com.example.studentvotingapp.models.DBModel
import com.example.studentvotingapp.models.DatabaseStructure
import com.example.studentvotingapp.models.User
import com.example.studentvotingapp.models.Vote
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.base.BaseService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VoteService(dbContext: FirebaseFirestore? = null) : BaseService(dbContext) {
    fun getAllVotes(onSuccess: (Map<String, Vote>) -> Unit, onFailure: (Exception) -> Unit) {
        this.getAllDocuments<Vote>(DBModel.Vote, onSuccess, onFailure)
    }

    fun addVote(vote: Vote, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        this.addDocument(DBModel.Vote, vote, onSuccess, onFailure)
    }

    fun getVotesByUserId(onSuccess: (Map<String, Vote>) -> Unit) {
        dbContext.collection("votes")
            .whereEqualTo("userUid", GlobalUserData.user.uid)
            .get()
            .addOnSuccessListener { result ->
                val documents = result.associate { it.id to it.toObject(Vote::class.java) }
                onSuccess(documents)
            }
    }
}
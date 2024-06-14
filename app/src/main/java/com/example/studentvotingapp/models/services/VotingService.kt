package com.example.studentvotingapp.models.services

import com.example.studentvotingapp.models.DBModel
import com.example.studentvotingapp.models.User
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.base.BaseService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class VotingService(dbContext: FirebaseFirestore? = null) : BaseService(dbContext) {
    fun getAllVotings(onSuccess: (Map<String, Voting>) -> Unit, onFailure: (Exception) -> Unit) {
        this.getAllDocuments<Voting>(DBModel.Voting, onSuccess, onFailure)
    }

    fun addVoting(voting: Voting, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        this.addDocument(DBModel.Voting, voting, onSuccess, onFailure)
    }

    fun getAvailableVotings(excludeVotings: List<String>, onSuccess: (Map<String, Voting>) -> Unit) {
        val currentTime = Timestamp.now()
        dbContext.collection("votings")
            .whereGreaterThan("endTime", currentTime)
            //.whereLessThanOrEqualTo("startTime", currentTime)
            //.orderBy("endDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                var documents = result.associate { it.id to it.toObject(Voting::class.java) }
                documents = documents.filter { !excludeVotings.contains(it.key) }
                onSuccess(documents)
            }
    }

    fun getFinishedVotings(onSuccess: (Map<String, Voting>) -> Unit) {
        val currentTime = Timestamp.now()
        dbContext.collection("votings")
            .whereLessThan("endTime", currentTime)
            //.orderBy("endDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val documents = result.associate { it.id to it.toObject(Voting::class.java) }
                onSuccess(documents)
            }
    }

    fun updateVotingCandidate(votingId: String, candidate: String) {
        dbContext.collection("votings").document(votingId)
            .update("votes.$candidate", FieldValue.increment(1))
            .addOnSuccessListener {
                //Toast.makeText(context, "Voted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                //Toast.makeText(context, "Failed to vote", Toast.LENGTH_SHORT).show()
            }
    }
}
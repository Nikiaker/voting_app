package com.example.studentvotingapp.models

data class Voting (
    val title: String = "",
    val description: String = "",
    val startTime: com.google.firebase.Timestamp? = null,
    val endTime: com.google.firebase.Timestamp? = null,
    val candidates: List<String> = listOf(),
    val votes: Map<String, Int> = mapOf()
)
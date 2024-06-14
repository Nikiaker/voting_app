package com.example.studentvotingapp.models

data class User(
    val uid: String = "",
    val username: String = "",
    val admin: Boolean = false,
    val hasAccess: Boolean = false
)
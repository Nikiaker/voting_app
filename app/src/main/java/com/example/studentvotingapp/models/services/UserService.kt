package com.example.studentvotingapp.models.services

import android.provider.ContactsContract.Data
import com.example.studentvotingapp.models.DBModel
import com.example.studentvotingapp.models.DatabaseStructure
import com.example.studentvotingapp.models.User
import com.example.studentvotingapp.models.services.base.BaseService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class UserService(dbContext: FirebaseFirestore? = null) : BaseService(dbContext) {
    fun getAllUsers(onSuccess: (Map<String, User>) -> Unit, onFailure: (Exception) -> Unit) {
        this.getAllDocuments<User>(DBModel.User, onSuccess, onFailure)
    }

    fun addUser(user: User, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        this.addDocument(DBModel.User, user, onSuccess, onFailure)
    }

    fun getUserByUid(uid: String, onSuccess: (Map<String, User>) -> Unit) {
        dbContext.collection(DatabaseStructure.getCollection(DBModel.User))
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { result ->
                val documents = result.associate { it.id to it.toObject(User::class.java) }
                onSuccess(documents)
            }
    }

    fun addUserWithUid(uid: String, onSuccess: (String) -> Unit) {
        val user = User(
            uid = uid,
            username = uid
        )
        this.addUser(user, onSuccess) {}
    }
}
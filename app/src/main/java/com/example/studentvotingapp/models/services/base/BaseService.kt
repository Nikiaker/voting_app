package com.example.studentvotingapp.models.services.base

import com.example.studentvotingapp.models.DBModel
import com.example.studentvotingapp.models.DatabaseStructure
import com.example.studentvotingapp.models.User
import com.example.studentvotingapp.models.Vote
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class BaseService(dbContext: FirebaseFirestore?) {
    protected var dbContext: FirebaseFirestore

    init {
        if (dbContext != null) {
            this.dbContext = dbContext
        }
        else {
            this.dbContext = Firebase.firestore
        }
    }

    protected inline fun <reified T: Any> getAllDocuments(dbModel: DBModel, crossinline onSuccess: (Map<String, T>) -> Unit, crossinline onFailure: (Exception) -> Unit) {
        this.dbContext.collection(DatabaseStructure.getCollection(dbModel)).get()
            .addOnSuccessListener { result ->
                val documents = result.associate { it.id to it.toObject(T::class.java) }
                onSuccess(documents)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    protected fun <T: Any> addDocument(dbModel: DBModel, document: T, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        this.dbContext.collection(DatabaseStructure.getCollection(dbModel)).add(document)
            .addOnSuccessListener { result ->
                onSuccess(result.id)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
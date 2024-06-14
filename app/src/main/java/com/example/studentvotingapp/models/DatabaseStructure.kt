package com.example.studentvotingapp.models

enum class DBModel {
    User,
    Vote,
    Voting,
}

class DatabaseStructure {
    companion object {
        private val structure = mapOf(
            DBModel.User to "users",
            DBModel.Vote to "votes",
            DBModel.Voting to "votings",
        )

        fun getCollection(model: DBModel): String {
            return if (this.structure[model] != null) {
                this.structure[model]!!
            } else {
                "unknown"
            }
        }
    }

}
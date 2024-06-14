package com.example.studentvotingapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class GreetingScreenView {
    companion object {
        @Composable
        fun GreetingScreen(navController: androidx.navigation.NavController) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome to the Student Voting App")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("voting") }) {
                    Text(text = "Start Voting")
                }
                Button(onClick = { navController.navigate("voteTestAdd") }) {
                    Text(text = "Add Test Vote")
                }
                Button(onClick = {navController.navigate("voteTestList")}) {
                    Text(text = "See Test Vote List")
                }
            }
        }
    }
}
package com.example.studentvotingapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.auth.AuthResult

class LoginScreenView {
    companion object {
        @Composable
        fun LoginScreen(navController: androidx.navigation.NavController, loggedInCallback: (AuthResult) -> Unit?) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var errorMessage by remember { mutableStateOf("") }

            val auth = Firebase.auth

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        errorMessage = "You must provide login and password"
                    }
                    else {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    loggedInCallback(task.result)
                                } else {
                                    errorMessage = task.exception?.message ?: "Login failed"
                                }
                            }
                    }
                }) {
                    Text("Login")
                }
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
                TextButton(onClick = { navController.navigate("registerScreen") }) {
                    Text("Don't have an account? Register")
                }
            }
        }
    }
}
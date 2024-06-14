// MainActivity.kt
package com.example.studentvotingapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.UserService
import com.example.studentvotingapp.ui.theme.StudentVotingAppTheme
import com.example.studentvotingapp.views.AvailableVotesScreenView
import com.example.studentvotingapp.views.FinishedVotesScreenView
import com.example.studentvotingapp.views.GenerateQRCodeScreenView
import com.example.studentvotingapp.views.GreetingScreenView
import com.example.studentvotingapp.views.LoginScreenView
import com.example.studentvotingapp.views.QRCodeScannerScreenView
import com.example.studentvotingapp.views.RegisterScreenView
import com.example.studentvotingapp.views.VotingResultsScreenView
import com.example.studentvotingapp.views.VotingTestAddView
import com.example.studentvotingapp.views.VotingScreenView
import com.example.studentvotingapp.views.VotingTestListView
import com.example.studentvotingapp.views.components.BottomNavigationBarView
import com.example.studentvotingapp.views.dialogs.ExceptionDialogView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentVotingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavHost()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost() {
    var isException by remember { mutableStateOf(false) }
    val exceptionCallback = { isException = false }
    var exceptionMessage by remember { mutableStateOf("") }

    if (isException) {
        ExceptionDialogView.ExceptionDialog(exceptionMessage = exceptionMessage, onDismiss = exceptionCallback)
    }

    val navController = rememberNavController()
    var loggedIn by remember { mutableStateOf(false) }
    val loggedInCallback = { result: AuthResult ->
        val userService = UserService()
        val uid = result.user!!.uid
        userService.getUserByUid(uid) { users ->
            if (users.isNotEmpty()) {
                users.forEach { GlobalUserData.user = it.value }
                loggedIn = true
            }
            else {
                exceptionMessage = "For some reason the user is not associated with the database. Weird"
                isException = true
                logOut()
            }
        }
    }
    var currentTab by remember { mutableStateOf("") }

    val auth = Firebase.auth
    if (auth.currentUser != null && auth.uid != null) {
        val userService = UserService()
        val uid = auth.uid!!
        userService.getUserByUid(uid) { users ->
            if (users.isNotEmpty()) {
                users.forEach { GlobalUserData.user = it.value }
                loggedIn = true
            } else {
                exceptionMessage =
                    "For some reason the user is not associated with the database. Weird"
                isException = true
                logOut()
            }
        }
    }

    if (!loggedIn) {
        NavHost(navController = navController, startDestination = "loginScreen") {
            composable("loginScreen") { LoginScreenView.LoginScreen(navController, loggedInCallback) }
            composable("registerScreen") { RegisterScreenView.RegisterScreen(navController) }
        }
    }
    else {
        if (GlobalUserData.user.hasAccess) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Voting App") },
                        navigationIcon = {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                }
                        },
                        actions = {
                            UserIcon {
                                loggedIn = false
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomNavigationBarView.BottomNavigationBar(
                        selectedScreen = currentTab,
                        onTabSelected = {
                            currentTab = it
                            navController.navigate(it)
                        }
                    )
                }
            ) { it ->
                Column( modifier = Modifier.padding(it)) {
                    var voting: Pair<String, Voting> = Pair("", Voting())
                    var votingResult: Pair<String, Voting> = Pair("", Voting())
                    NavHost(navController = navController, startDestination = "available") {
                        composable("available") {
                            AvailableVotesScreenView.AvailableVotesScreen { vo ->
                            voting = vo
                            navController.navigate("voting")
                            }
                        }
                        composable("finished") {
                            FinishedVotesScreenView.FinishedVotesScreen {
                            votingResult = it
                            navController.navigate("voting_result")
                            }
                        }
                        composable("greeting") { GreetingScreenView.GreetingScreen(navController) }
                        composable("voting") { VotingScreenView.VotingScreen(voting) { navController.popBackStack() } }
                        composable("voting_result") { VotingResultsScreenView.VotingResultsScreen(votingResult.second) { navController.popBackStack() } }
                        composable("generate_qr_list") {
                            AvailableVotesScreenView.AvailableVotesScreen { vo ->
                                voting = vo
                                navController.navigate("generate_qr")
                            }
                        }
                        composable("generate_qr") { GenerateQRCodeScreenView.GenerateQRCodeScreen(voting) }
                        composable("add_vote") { VotingTestAddView.AddVotingScreen() }
                        composable("voteTestList") { VotingTestListView.VotingsListScreen() }
                        composable("qr_scanner") { QRCodeScannerScreenView.QRCodeScannerScreen() }
                    }
                }
            }
        }
        else {
            Text("We are sorry but your account does not have access to this app. Contact the administrator to gain access.")
        }
    }
}

@Composable
fun UserIcon(onLogout: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Default.AccountCircle, contentDescription = "User Icon", modifier = Modifier.size(24.dp))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    logOut()
                    onLogout()
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

fun logOut() {
    val auth = Firebase.auth
    auth.signOut()
    // Clear user credentials if you have implemented local storage for them
    // clearUserCredentials(context)
}
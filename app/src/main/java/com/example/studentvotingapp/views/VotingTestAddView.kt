package com.example.studentvotingapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentvotingapp.models.Voting
import com.example.studentvotingapp.models.services.VotingService
import com.example.studentvotingapp.views.components.DatePickerDialogView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class VotingTestAddView {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun AddVotingScreen() {
            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var startTime by remember { mutableStateOf("") }
            var endTime by remember { mutableStateOf("") }
            var candidates by remember { mutableStateOf("") }

            var startDate by remember { mutableStateOf<LocalDate?>(null) }
            var endDate by remember { mutableStateOf<LocalDate?>(null) }
            var showStartDatePicker by remember { mutableStateOf(false) }
            var showEndDatePicker by remember { mutableStateOf(false) }

            var displayDialog by remember { mutableStateOf(false) }
            var dialogText by remember { mutableStateOf("") }

            if (displayDialog) {
                AlertDialog(
                    title = {
                        Text(text = "dialogTitle")
                    },
                    text = {
                        Text(text = dialogText)
                    },
                    onDismissRequest = {
                        displayDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                displayDialog = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                // Start Date Picker
                TextField(
                    value = startDate?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Start Date") },
                    readOnly = false,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartDatePicker = true }
                )
                // End Date Picker
                TextField(
                    value = endDate?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("End Date") },
                    readOnly = false,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndDatePicker = true }
                )
                TextField(
                    value = candidates,
                    onValueChange = { candidates = it },
                    label = { Text("Candidates (comma-separated)") }
                )
                Button(onClick = {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startTimestamp = sdf.parse(startDate.toString())?.let { Timestamp(it) }
                    val endTimestamp = sdf.parse(endDate.toString())?.let { Timestamp(it) }
                    val voting = Voting(
                        title = title,
                        description = description,
                        startTime = startTimestamp,
                        endTime = endTimestamp,
                        candidates = candidates.split(",").map { it.trim() },
                        votes = candidates.split(",").associateWith { 0 }
                    )
                    val votingService = VotingService()
                    votingService.addVoting(voting,
                        {
                            dialogText = "Successfully added the voting: $it"
                            displayDialog = true
                        },
                        {
                            dialogText = "There was an exception: ${it.message}"
                            displayDialog = true
                        })
                }) {
                    Text("Add Voting")
                }
            }
            if (showStartDatePicker) {
                DatePickerDialogView.DatePickerDialog(
                    initialDate = LocalDate.now(),
                    onDateSelected = { selectedDate ->
                        startDate = selectedDate
                        showStartDatePicker = false
                    },
                    onDismissRequest = { showStartDatePicker = false }
                )
            }

            if (showEndDatePicker) {
                DatePickerDialogView.DatePickerDialog(
                    initialDate = LocalDate.now(),
                    onDateSelected = { selectedDate ->
                        endDate = selectedDate
                        showEndDatePicker = false
                    },
                    onDismissRequest = { showEndDatePicker = false }
                )
            }

        }

    }
}
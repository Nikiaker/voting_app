package com.example.studentvotingapp.views.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

class DatePickerDialogView {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun DatePickerDialog(
            initialDate: LocalDate,
            onDateSelected: (LocalDate) -> Unit,
            onDismissRequest: () -> Unit
        ) {
            val context = LocalContext.current
            val year = initialDate.year
            val month = initialDate.monthValue - 1 // Month is 0-based in DatePickerDialog
            val day = initialDate.dayOfMonth

            val datePickerDialog = remember {
                android.app.DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val selectedDate =
                            LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                        onDateSelected(selectedDate)
                    },
                    year,
                    month,
                    day
                ).apply {
                    setOnDismissListener { onDismissRequest() }
                }
            }

            DisposableEffect(Unit) {
                datePickerDialog.show()
                onDispose { datePickerDialog.dismiss() }
            }
        }

    }
}
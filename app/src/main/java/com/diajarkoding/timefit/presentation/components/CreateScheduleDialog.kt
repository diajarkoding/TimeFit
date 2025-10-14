package com.diajarkoding.timefit.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CreateScheduleDialog(
    newScheduleName: String,
    onNameChange: (String) -> Unit,
    onCreate: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Schedule") },
        text = {
            OutlinedTextField(
                value = newScheduleName,
                onValueChange = onNameChange,
                label = { Text("Schedule Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = onCreate,
                enabled = newScheduleName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
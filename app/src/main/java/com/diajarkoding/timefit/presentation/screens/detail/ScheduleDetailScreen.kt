package com.diajarkoding.timefit.presentation.screens.detail

import android.R.attr.enabled
import android.R.attr.type
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.diajarkoding.timefit.data.DummyData
import com.diajarkoding.timefit.data.local.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailScreen(
    navController: NavController,
    viewModel: ScheduleDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Tampilkan dialog jika state-nya true
    if (state.isAddExerciseDialogOpen) {
        AddExerciseDialog(
            state = state,
            onEvent = viewModel::onEvent
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.schedule?.name ?: "...") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = { IconButton(
                    onClick = { viewModel.onEvent(ScheduleDetailEvent.OnShowAddExerciseDialog) }
                ) {
                    Icon(Icons.Default.Add, "Add Exercise")
                    }
                }
            )
        }
//        floatingActionButton = {
//            FloatingActionButton(onClick = { viewModel.onEvent(ScheduleDetailEvent.OnShowAddExerciseDialog) }) {
//                Icon(Icons.Default.Add, "Add Exercise")
//            }
//        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(state.exercises, key = { it.id }) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    onDelete = { viewModel.onEvent(ScheduleDetailEvent.OnDeleteExercise(exercise)) }
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(exercise.name, fontWeight = FontWeight.Bold)
            Text("Sets: ${exercise.sets} | Reps: ${exercise.reps} | Weight: ${exercise.weight}kg | Rest: ${exercise.rest}s")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, "Delete Exercise", tint = MaterialTheme.colorScheme.error)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseDialog(
    state: ScheduleDetailState,
    onEvent: (ScheduleDetailEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredExercises = remember(state.exercisesSearchQuery) {
        if (state.exercisesSearchQuery.isBlank()) {
            DummyData.exerciseList
        } else {
            DummyData.exerciseList.filter {
                it.contains(state.exercisesSearchQuery, ignoreCase = true)
            }
        }
    }

    AlertDialog(
        onDismissRequest = { onEvent(ScheduleDetailEvent.OnDismissAddExerciseDialog) },
        title = { Text("Add New Exercise") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Dropdown untuk Nama Latihan
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = state.exercisesSearchQuery,
                        onValueChange = {
                            query -> onEvent(ScheduleDetailEvent.OnExercisesSearchQueryChange(query))
                        },
                        readOnly = false,
                        label = { Text("Exercise") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                            .fillMaxWidth()
                    )
                    if (filteredExercises.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredExercises.forEach { exerciseName ->
                                DropdownMenuItem(
                                    text = { Text(exerciseName) },
                                    onClick = {
                                        onEvent(ScheduleDetailEvent.OnExerciseNameChange(exerciseName))
//                                        onEvent(ScheduleDetailEvent.OnExercisesSearchQueryChange(exerciseName))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Input Fields lainnya
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = state.sets, onValueChange = { onEvent(ScheduleDetailEvent.OnSetsChange(it)) }, label = { Text("Sets") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = state.reps, onValueChange = { onEvent(ScheduleDetailEvent.OnRepsChange(it)) }, label = { Text("Reps") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = state.weight, onValueChange = { onEvent(ScheduleDetailEvent.OnWeightChange(it)) }, label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = state.rest, onValueChange = { onEvent(ScheduleDetailEvent.OnRestChange(it)) }, label = { Text("Rest (s)") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            }
        },
        confirmButton = {
            Button(onClick = { onEvent(ScheduleDetailEvent.OnCreateExercise) }) { Text("Add") }
        },
        dismissButton = {
            Button(onClick = { onEvent(ScheduleDetailEvent.OnDismissAddExerciseDialog) }) { Text("Cancel") }
        }
    )
}
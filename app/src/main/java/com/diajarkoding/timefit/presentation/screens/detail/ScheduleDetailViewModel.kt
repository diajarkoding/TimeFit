package com.diajarkoding.timefit.presentation.screens.detail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diajarkoding.timefit.data.local.Exercise
import com.diajarkoding.timefit.data.repository.ScheduleRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val repository: ScheduleRepositoryImpl,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val scheduleId = savedStateHandle.get<Int>("scheduleId") ?: -1

    private val _state = MutableStateFlow(ScheduleDetailState())
    val state = _state.asStateFlow()

    init {
        // Ambil detail jadwal dan daftar latihannya secara bersamaan
        viewModelScope.launch {
            repository.getScheduleById(scheduleId)?.let { schedule ->
                _state.update { it.copy(schedule = schedule) }
            }
        }
        repository.getExercisesByScheduleId(scheduleId)
            .onEach { exercises ->
                _state.update { it.copy(exercises = exercises) }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ScheduleDetailEvent) {
        when (event) {
            // Logika untuk menampilkan dan menyembunyikan dialog
            is ScheduleDetailEvent.OnShowAddExerciseDialog -> {
                _state.update { it.copy(isAddExerciseDialogOpen = true) }
            }
            is ScheduleDetailEvent.OnDismissAddExerciseDialog -> {
                resetDialogState()
            }
            // Logika untuk menyimpan input dari form
            is ScheduleDetailEvent.OnExerciseNameChange -> _state.update { it.copy(exerciseName = event.name, exercisesSearchQuery = event.name) }
            is ScheduleDetailEvent.OnSetsChange -> _state.update { it.copy(sets = event.sets) }
            is ScheduleDetailEvent.OnRepsChange -> _state.update { it.copy(reps = event.reps) }
            is ScheduleDetailEvent.OnWeightChange -> _state.update { it.copy(weight = event.weight) }
            is ScheduleDetailEvent.OnRestChange -> _state.update { it.copy(rest = event.rest) }
            // Logika untuk membuat Exercise baru
            is ScheduleDetailEvent.OnSaveExercise -> {
                val currentState = _state.value

                // Tentukan apakah ini 'update' atau 'insert'
                if (currentState.exerciseToEdit != null) {
                    // Ini mode UPDATE
                    val updatedExercise = currentState.exerciseToEdit.copy(
                        name = currentState.exerciseName,
                        sets = currentState.sets.toIntOrNull() ?: 0,
                        reps = currentState.reps.toIntOrNull() ?: 0,
                        weight = currentState.weight.toDoubleOrNull() ?: 0.0,
                        rest = currentState.rest.toIntOrNull() ?: 0
                    )
                    viewModelScope.launch {
                        repository.updateExercise(updatedExercise)
                    }
                } else {
                    // Ini mode CREATE (logika lama)
                    val newExercise = Exercise(
                        scheduleId = scheduleId,
                        name = currentState.exerciseName,
                        sets = currentState.sets.toIntOrNull() ?: 0,
                        reps = currentState.reps.toIntOrNull() ?: 0,
                        weight = currentState.weight.toDoubleOrNull() ?: 0.0,
                        rest = currentState.rest.toIntOrNull() ?: 0
                    )
                    viewModelScope.launch {
                        repository.insertExercise(newExercise)
                    }
                }
                resetDialogState()
            }
            // Logika untuk mengedit Exercise
            is ScheduleDetailEvent.OnEditExerciseClick -> {
                val exerciseToEdit = event.exercise
                _state.update {
                    it.copy(
                        isAddExerciseDialogOpen = true,
                        exerciseName = exerciseToEdit.name,
                        sets = exerciseToEdit.sets.toString(),
                        reps = exerciseToEdit.reps.toString(),
                        weight = exerciseToEdit.weight.toString(),
                        rest = exerciseToEdit.rest.toString(),
                        exerciseToEdit = exerciseToEdit
                    )
                }
            }
            // Logika untuk menghapus exercise
            is ScheduleDetailEvent.OnDeleteExercise -> {
                viewModelScope.launch {
                    repository.deleteExercise(event.exercise)
                }
            }
            is ScheduleDetailEvent.OnExercisesSearchQueryChange -> {
                _state.update { it.copy(exercisesSearchQuery = event.query) }
            }
        }
    }

    private fun resetDialogState() {
        _state.update {
            it.copy(
                isAddExerciseDialogOpen = false,
                exerciseName = "", sets = "", reps = "", weight = "", rest = ""
            )
        }
    }
}
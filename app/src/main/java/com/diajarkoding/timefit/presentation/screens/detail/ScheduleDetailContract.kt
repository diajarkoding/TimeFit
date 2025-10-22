package com.diajarkoding.timefit.presentation.screens.detail


import com.diajarkoding.timefit.data.local.Exercise
import com.diajarkoding.timefit.data.local.Schedule

data class ScheduleDetailState(
    val schedule: Schedule? = null,
    val exercises: List<Exercise> = emptyList(),
    val isAddExerciseDialogOpen: Boolean = false,

    val exerciseToEdit: Exercise? = null,

    // State untuk dialog tambah latihan
    val exerciseName: String = "",
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val rest: String = "",

    val exercisesSearchQuery: String = ""
)

sealed class ScheduleDetailEvent {
    data class OnDeleteExercise(val exercise: Exercise) : ScheduleDetailEvent()

    data class OnEditExerciseClick(val exercise: Exercise) : ScheduleDetailEvent()

    // Event untuk dialog
    object OnShowAddExerciseDialog : ScheduleDetailEvent()
    object OnDismissAddExerciseDialog : ScheduleDetailEvent()
    object OnSaveExercise : ScheduleDetailEvent()
    data class OnExerciseNameChange(val name: String) : ScheduleDetailEvent()
    data class OnSetsChange(val sets: String) : ScheduleDetailEvent()
    data class OnRepsChange(val reps: String) : ScheduleDetailEvent()
    data class OnWeightChange(val weight: String) : ScheduleDetailEvent()
    data class OnRestChange(val rest: String) : ScheduleDetailEvent()
    data class OnExercisesSearchQueryChange(val query: String) : ScheduleDetailEvent()
}
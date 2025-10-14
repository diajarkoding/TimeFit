package com.diajarkoding.timefit.presentation.screens.home

import com.diajarkoding.timefit.data.local.Schedule

data class HomeScreenState(
    val schedules: List<Schedule> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCreateDialogOpen: Boolean = false,
    val newScheduleName: String = ""
)

sealed class HomeScreenEvent {
    data class OnDeleteSchedule(val schedule: Schedule) : HomeScreenEvent()
    data class OnScheduleNameChange(val name: String) : HomeScreenEvent()
    object OnCreateSchedule : HomeScreenEvent()
    object OnShowCreateDialog : HomeScreenEvent()
    object OnDismissCreateDialog : HomeScreenEvent()
}
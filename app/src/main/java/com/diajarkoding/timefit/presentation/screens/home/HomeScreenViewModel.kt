package com.diajarkoding.timefit.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diajarkoding.timefit.data.local.Schedule
import com.diajarkoding.timefit.data.repository.ScheduleRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private  val repository: ScheduleRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        getAllSchedules()
    }

    private fun getAllSchedules() {
        repository.getAllSchedules().onEach { schedules ->
            _state.update { it.copy(schedules = schedules) }
        }.launchIn(viewModelScope) }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnDeleteSchedule -> {
                viewModelScope.launch {
                    repository.deleteSchedule(event.schedule)
                }
            }
            // Logika untuk menampilkan dialog
            HomeScreenEvent.OnShowCreateDialog -> {
                _state.update { it.copy(isCreateDialogOpen = true) }
            }
            // Logika untuk menyembunyikan dialog
            HomeScreenEvent.OnDismissCreateDialog -> {
                _state.update { it.copy(isCreateDialogOpen = false, newScheduleName = "") }
            }
            // Logika untuk menyimpan input dari TextField
            is HomeScreenEvent.OnScheduleNameChange -> {
                _state.update { it.copy(newScheduleName = event.name) }
            }
            // Logika untuk membuat dan menyimpan jadwal baru
            HomeScreenEvent.OnCreateSchedule -> {
                val scheduleName = _state.value.newScheduleName
                if (scheduleName.isBlank()) {
                    return // Jangan lakukan apa-apa jika nama kosong
                }

                val newSchedule = Schedule(name = scheduleName)
                viewModelScope.launch {
                    repository.insertSchedule(newSchedule)
                }
                // Reset state setelah membuat jadwal
                _state.update { it.copy(isCreateDialogOpen = false, newScheduleName = "") }
            }
        }
    }
}
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
    private  val respository: ScheduleRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        getAllSchedules()
    }

    private fun getAllSchedules() {
        respository.getAllSchedules().onEach { schedules ->
            _state.update { it.copy(schedules = schedules) }
        }.launchIn(viewModelScope) }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnDeleteSchedule -> {
                viewModelScope.launch{
                    respository.deleteSchedule(event.schedule)
                }
            }
            is HomeScreenEvent.OnScheduleNameChange -> {
                _state.update { it.copy() }
            }
            is HomeScreenEvent.OnCreateSchedule -> {
                val name = _state.value.toString().ifBlank { "New Schedule" }
                viewModelScope.launch {
                    respository.insertSchedule(Schedule(name = name))
                }
                _state.update { it.copy() }
            }
            is HomeScreenEvent.OnShowCreateDialog -> {
                _state.update { it.copy() }
            }
            is HomeScreenEvent.OnDismissDialog -> {
                _state.update { it.copy() }
            }
        }
    }
}
package com.diajarkoding.timefit.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.diajarkoding.timefit.data.local.Schedule
import com.diajarkoding.timefit.data.repository.ScheduleRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val repository: ScheduleRepositoryImpl,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<Schedule?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val scheduleId = savedStateHandle.get<Int>("scheduleId") ?: -1
            val schedule = repository.getScheduleById(scheduleId)
            _state.update { schedule }
        }
    }
}
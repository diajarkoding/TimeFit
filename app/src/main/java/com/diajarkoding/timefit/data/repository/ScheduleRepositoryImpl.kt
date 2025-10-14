package com.diajarkoding.timefit.data.repository

import com.diajarkoding.timefit.data.local.Schedule
import com.diajarkoding.timefit.data.local.ScheduleDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val dao: ScheduleDao
) {
    fun getAllSchedules(): Flow<List<Schedule>> {
        return dao.getAllSchedules()
    }

    suspend fun insertSchedule(schedule: Schedule) {
        dao.insertSchedule(schedule)
    }

    suspend fun deleteSchedule(schedule: Schedule) {
        dao.deleteSchedule(schedule)
    }

    suspend fun getScheduleById(id: Int): Schedule? {
        return dao.getScheduleById(id)
    }
}
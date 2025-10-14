package com.diajarkoding.timefit.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedules ORDER BY id DESC")
    fun getAllSchedules(): Flow<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun getScheduleById(id: Int): Schedule?
}
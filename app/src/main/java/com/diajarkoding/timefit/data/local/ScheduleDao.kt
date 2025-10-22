package com.diajarkoding.timefit.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises WHERE scheduleId = :scheduleId ORDER BY id DESC")
    fun getExercisesByScheduleId(scheduleId: Int): Flow<List<Exercise>>

    // --- Workout Session & Log Operations ---

    @Insert
    suspend fun insertWorkoutSession(session: WorkoutSession): Long

    @Update
    suspend fun updateWorkoutSession(session: WorkoutSession)

    @Insert
    suspend fun insertWorkoutLog(log: WorkoutLog)

    @Query("SELECT * FROM workout_logs WHERE sessionId = :sessionId ORDER BY timestamp DESC")
    fun getLogsForSession(sessionId: Int): Flow<List<WorkoutLog>>
}
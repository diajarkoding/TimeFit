package com.diajarkoding.timefit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Schedule::class, Exercise::class],
    version = 2
)
abstract class TimeFitDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}
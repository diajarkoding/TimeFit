package com.diajarkoding.timefit.data.local

import androidx.room.Database

@Database(
    entities = [Schedule::class],
    version = 1
)
abstract class TimeFitDatabase : androidx.room.RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}
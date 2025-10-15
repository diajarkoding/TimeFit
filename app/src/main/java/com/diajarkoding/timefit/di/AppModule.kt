package com.diajarkoding.timefit.di

import android.app.Application
import androidx.room.Room
import com.diajarkoding.timefit.data.local.TimeFitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTimeFitDatabase(app: Application): TimeFitDatabase {
        return Room.databaseBuilder(
            app,
            TimeFitDatabase::class.java,
            "timefit_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideScheduleDao(db: TimeFitDatabase) = db.scheduleDao()
}
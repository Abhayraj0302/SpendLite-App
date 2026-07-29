package com.example.spendlite.di

import android.content.Context
import androidx.room.Room
import com.example.spendlite.data.local.AppDatabase
import com.example.spendlite.data.local.Dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "spendlite.db"
        ).build()
    }

    @Provides
    fun provideExpenseDao(
        database: AppDatabase
    ): ExpenseDao {
        return database.expenseDao()
    }
}

package com.example.spendlite.di

import android.content.Context
import androidx.room.Room
import com.example.spendlite.core.utils.AuthConstants
import com.example.spendlite.data.local.AppDatabase
import com.example.spendlite.data.local.Dao.ExpenseDao
import com.example.spendlite.data.remote.ExpenseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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


    @Provides
    @Singleton
    fun provideExpenseApi(): ExpenseApi {
        return Retrofit.Builder()
            .baseUrl(AuthConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()
            .create(ExpenseApi::class.java)
    }
}

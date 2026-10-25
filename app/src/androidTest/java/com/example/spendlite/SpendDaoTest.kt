package com.example.spendlite

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.spendlite.data.local.AppDatabase
import com.example.spendlite.data.local.Dao.ExpenseDao
import org.junit.After
import org.junit.Before

class SpendDaoTest {
    lateinit var spenddatabase: AppDatabase
    lateinit var spendDaoTest: ExpenseDao

    @Before
    fun setup() {
        spenddatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        spendDaoTest = spenddatabase.expenseDao()
    }

    @After
    fun tearDown() {
        spenddatabase.close()
    }
}
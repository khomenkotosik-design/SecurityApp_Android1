package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Вказуємо, які таблиці (entities) входять до бази та версію схеми
@Database(entities = [SecurityLog::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Метод для доступу до функцій запитів (DAO)
    abstract fun logDao(): LogDao

    companion object {
        // @Volatile гарантує, що значення INSTANCE завжди актуальне для всіх потоків
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Паттерн Singleton: створюємо базу лише один раз за весь час роботи програми
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "security_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
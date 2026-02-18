package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LogDao {
    // Метод для додавання нового запису про тривогу
    @Insert
    suspend fun insert(log: SecurityLog)

    // Метод для отримання всіх записів, відсортованих від нових до старих
    @Query("SELECT * FROM security_logs ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<SecurityLog>
}
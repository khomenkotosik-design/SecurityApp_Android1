package com.example.myapplication.services

import android.app.*
import android.content.Intent
import android.hardware.*
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.SecurityLog
import kotlinx.coroutines.*

class SecurityService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Реєструємо датчик (Вимога №2)
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        startForeground(1, createNotification("Охорона активна"))
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x = event?.values?.get(0) ?: 0f
        if (Math.abs(x) > 15f) { // Якщо зафіксовано сильний рух
            saveAlarmToDatabase()
        }
    }

    private fun saveAlarmToDatabase() {
        serviceScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.logDao().insert(SecurityLog(
                timestamp = System.currentTimeMillis(),
                eventType = "ТРИВОГА",
                message = "Зафіксовано рух!"
            ))
        }
    }

    private fun createNotification(text: String): Notification {
        val channelId = "security_channel"
        val channel = NotificationChannel(channelId, "Security", NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Система безпеки")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
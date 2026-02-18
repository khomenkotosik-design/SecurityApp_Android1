package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class LogFragment : Fragment(R.layout.fragment_main) { // Використовуємо твій макет
    private val fragmentScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvLog = view.findViewById<TextView>(R.id.tvStatus) // Використаємо існуючий TextView для тесту

        fragmentScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val logs = withContext(Dispatchers.IO) { db.logDao().getAllLogs() }

            val logText = StringBuilder()
            val sdf = SimpleDateFormat("dd.MM HH:mm:ss", Locale.getDefault())

            logs.forEach {
                logText.append("${sdf.format(Date(it.timestamp))} - ${it.eventType}: ${it.message}\n")
            }
            tvLog.text = if (logs.isEmpty()) "Лог порожній" else logText.toString()
        }
    }
}
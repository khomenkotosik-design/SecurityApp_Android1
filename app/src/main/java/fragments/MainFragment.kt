package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.utils.HashUtils
import com.example.myapplication.services.SecurityService

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val btnToggle = view.findViewById<Button>(R.id.btnToggle)

        // Хеш для ПІН-коду "1234" (Вимога №4)
        val correctPinHash = HashUtils.sha256("1234")

        btnToggle.setOnClickListener {
            val userEnteredPin = "1234" // Тимчасова імітація вводу
            val enteredHash = HashUtils.sha256(userEnteredPin)

            if (enteredHash == correctPinHash) {
                tvStatus.text = "ОХОРОНА ЗАПУЩЕНА"

                // Запуск фонового сервісу (Вимога №2)
                val intent = Intent(requireContext(), SecurityService::class.java)
                requireActivity().startForegroundService(intent)
            } else {
                tvStatus.text = "НЕВІРНИЙ ПІН-КОД"
            }
        }
    } // Закриваємо onViewCreated
} // Закриваємо клас
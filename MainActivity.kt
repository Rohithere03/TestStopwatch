package com.example.teststopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    private var handler = Handler()
    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L
    private var timePaused: Long = 0L
    private var isRunning = false

    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedTime = SystemClock.uptimeMillis() - startTime
            val totalElapsed = timePaused + elapsedTime

            val seconds = (totalElapsed / 1000).toInt()
            val minutes = seconds / 60
            val milliseconds = ((totalElapsed % 1000) / 10).toInt()
            //Limited the milliseconds from 0 to 99

            timerTextView.text = String.format("%02d:%02d:%02d", minutes, seconds % 60, milliseconds)

            handler.postDelayed(this, 10)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.chronometer)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis()
                handler.post(timerRunnable)
                isRunning = true
            }
        }

        stopButton.setOnClickListener {
            if (isRunning) {
                timePaused += elapsedTime
                handler.removeCallbacks(timerRunnable)
                isRunning = false
            }
        }

        resetButton.setOnClickListener {
            startTime = 0L
            elapsedTime = 0L
            timePaused = 0L
            timerTextView.text = "00:00:00"
            handler.removeCallbacks(timerRunnable)
            isRunning = false
        }
    }
}

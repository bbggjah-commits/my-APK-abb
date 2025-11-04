package com.waterreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var intervalInput: TextInputEditText
    private lateinit var startButton: MaterialButton
    private lateinit var stopButton: MaterialButton

    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 100
        private const val WORK_NAME = "water_reminder_work"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        intervalInput = findViewById(R.id.intervalInput)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        // Check and request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }

        // Set up button listeners
        startButton.setOnClickListener {
            startReminders()
        }

        stopButton.setOnClickListener {
            stopReminders()
        }
    }

    private fun startReminders() {
        val intervalText = intervalInput.text.toString()
        
        if (intervalText.isEmpty()) {
            Toast.makeText(this, "الرجاء إدخال الفترة الزمنية", Toast.LENGTH_SHORT).show()
            return
        }

        val intervalMinutes = intervalText.toLongOrNull()
        
        if (intervalMinutes == null || intervalMinutes < 1) {
            Toast.makeText(this, "الرجاء إدخال رقم صحيح أكبر من 0", Toast.LENGTH_SHORT).show()
            return
        }

        // Create periodic work request
        val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            intervalMinutes,
            TimeUnit.MINUTES
        ).build()

        // Schedule the work
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

        Toast.makeText(
            this,
            getString(R.string.reminders_started),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun stopReminders() {
        // Cancel the scheduled work
        WorkManager.getInstance(this).cancelUniqueWork(WORK_NAME)
        
        Toast.makeText(
            this,
            getString(R.string.reminders_stopped),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "تم منح إذن الإشعارات", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "يحتاج التطبيق إلى إذن الإشعارات للعمل بشكل صحيح",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

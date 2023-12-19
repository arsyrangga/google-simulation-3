package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import androidx.work.*
import com.dicoding.habitapp.utils.HABIT_TITLE
import androidx.work.WorkManager
import androidx.work.Data
import com.dicoding.habitapp.utils.HABIT_ID


class CountDownActivity : AppCompatActivity() {
    private lateinit var tv_count_down: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"
        tv_count_down = findViewById(R.id.tv_count_down)

        val habit = getParcelableExtra(intent, HABIT, Habit::class.java)

        if (habit != null) {
            findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

            val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

            //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
            viewModel.setInitialTime(habit.minutesFocus)
            viewModel.currentTimeString.observe(this) {
                tv_count_down.text = it
                //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
                if (it == "00:00") {
                    val data = Data.Builder()
                        .putString(HABIT_TITLE, habit.title)
                        .putInt(HABIT_ID, habit.id)
                        .build()
                    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>().setInputData(data).build()

                    WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                        habit.id.toString(), ExistingWorkPolicy.REPLACE, workRequest
                    )

                    updateButtonState(false)
                }
            }



            findViewById<Button>(R.id.btn_start).setOnClickListener {
                viewModel.startTimer()
                updateButtonState(true)
            }

            findViewById<Button>(R.id.btn_stop).setOnClickListener {
                viewModel.resetTimer()
                updateButtonState(false)

            }
        }

    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}
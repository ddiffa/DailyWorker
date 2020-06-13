package com.hellodiffa.dailyworker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, 7)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(DailyWorker::class.java)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext)
                .getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this@MainActivity,
                        Observer { workInfo ->
                            val status = workInfo.state.name
                            tvStatus.append("\n" + status)
                        })

        tvStatus.setOnClickListener {
            WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)
        }
    }
}
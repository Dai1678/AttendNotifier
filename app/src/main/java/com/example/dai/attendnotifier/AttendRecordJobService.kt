package com.example.dai.attendnotifier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AttendRecordJobService : JobService(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onStopJob(params: JobParameters?): Boolean {
        jobFinished(params, false)
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        job = Job()
        startNotification(params)
        return true
    }

    private fun startNotification(params: JobParameters?) = launch {
        withContext(Dispatchers.IO) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val name = "通知のタイトル"
            val id = "attend_notification_channel"
            val notifyDescription = "通知の詳細"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (manager.getNotificationChannel(id) == null) {
                    val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
                    channel.apply {
                        description = notifyDescription
                    }
                    manager.createNotificationChannel(channel)
                }
            }

            val notification = NotificationCompat.Builder(applicationContext, id).apply {
                setSmallIcon(R.drawable.ic_launcher_background)
                setContentTitle("タイトル")
                setContentText("内容")
            }.build()

            manager.notify(1, notification)

            jobFinished(params, false)
        }
    }

    companion object {
        private lateinit var job: Job
    }

}
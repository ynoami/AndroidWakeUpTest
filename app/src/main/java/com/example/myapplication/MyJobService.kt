package com.example.myapplication

import android.app.Notification
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.util.Log

class MyJobService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {

        jobFinished(params, false)
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        Thread(
            Runnable {
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = Notification.Builder(applicationContext)
                    .apply {
                        setContentTitle("JobSchedulerの通知")
                        setContentText("スケジュールで呼び出された処理で通知")
                        setSmallIcon(R.drawable.ic_launcher_background)
                    }.build()

                manager.notify(1, notification)
                Log.i(javaClass.name, "スケジュールしたジョブで呼び出された処理")

                jobFinished(params,false)
            }
        ).start()

        return true
    }

}
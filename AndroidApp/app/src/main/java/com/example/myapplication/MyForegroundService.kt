package com.example.myapplication

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.lang.Exception


class MyForegroundService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private var channelId: String = "DebnTestServiceChannel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initializeChannel(channelId, "DebnTestServiceChannel", "Careテスト用チャンネル")

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("ForegroundServiceの通知")
            .setContentText("定期実行サービスで呼び出された処理で通知")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        Thread(
            Runnable {
                while (true) {
                    Thread.sleep(10 * 1000)

                    Action()


                    Log.i(javaClass.name, "スケジュールしたジョブで呼び出された処理")
                }

                //stopForeground(true)
                // もしくは
                // stopSelf()

            }).start()

        startForeground(1, notification)

        return START_STICKY
    }

    private var _counter: Int = 0

    private fun Action() {
        //スリープから復帰
        val wakelock = (getSystemService(Context.POWER_SERVICE) as PowerManager)
            .newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        or  PowerManager.ACQUIRE_CAUSES_WAKEUP
                        or  PowerManager.ON_AFTER_RELEASE,
                 "myapp:mywakelocktag")
        wakelock.acquire(3000)

        // 通知の作成
        val intent = getTransitionIntent()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT)
        if (intent != null) {
            val notification = NotificationCompat
                .Builder(applicationContext, channelId)
                .apply {
                    setSmallIcon(R.mipmap.ic_launcher)
                    setContentTitle("MyApplication通知")
                    setContentText("イベント発生：" + _counter++)
                    setContentIntent(pendingIntent)
                }.build()
            getSystemService(Context.NOTIFICATION_SERVICE)
            val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.notify(1000, notification)
        }

        // chromeを起動
        //if (intent != null) {
        //    startActivity(intent)
        //}
    }

    private fun getTransitionIntent(): Intent? {
        val pm = packageManager
        try {
            //var className: String = "com.google.android.apps.chrome.Main"
            return pm.getLaunchIntentForPackage("com.android.chrome")
        } catch (e: Exception) {
            Log.i(javaClass.name, "インテント起動失敗")
            return null
        }
    }

    private fun initializeChannel(channelID: String, channelName: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(channelID, channelName, importance)
            mChannel.description = description
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}


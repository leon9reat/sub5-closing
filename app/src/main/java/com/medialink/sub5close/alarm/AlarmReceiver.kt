package com.medialink.sub5close.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.medialink.sub5close.MainActivity
import com.medialink.sub5close.R
import com.medialink.sub5close.data.movie.MovieRepository
import com.medialink.sub5close.notification.NotificationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val KEY_TYPE = "TYPE"
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "TMDB 5 Channel"
        private const val GROUP_KEY_EMAILS = "group_key_emails"

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"

        private val TAG = AlarmReceiver::class.java.simpleName
        const val ID_RELEASE = 100
        const val ID_DAILY = 101
        private const val REQUEST_MAIN_ACTIVITY = 200
    }

    private val movieRepo = MovieRepository()

    override fun onReceive(context: Context, intent: Intent) {
        val type_alarm = intent.getIntExtra(KEY_TYPE, 0)

        // hanya log biasa
        intent.action?.let {
            Log.d(TAG, "onReceive: " + intent.action)
        }

        if (type_alarm == ID_RELEASE) {

            // setting bahasa
            var lang = "en-US"
            if (Locale.getDefault().language.equals("in", ignoreCase = true)) lang =
                "id-ID"

            val format = SimpleDateFormat(DATE_FORMAT)
            val drTgl = format.format(Date())
            val spTgl = format.format(Date())
            CoroutineScope(Dispatchers.IO).launch {
                val list = movieRepo.findMovieToday(1, lang, drTgl, spTgl)
                list.results?.let {
                    val data = arrayListOf<NotificationItem>()
                    for (i in it.indices) {
                        data.add(NotificationItem(it[i].id ?: 0, "New Release Today", it[i].title ?: ""))
                    }
                    withContext(Dispatchers.Main) {
                        sendNotification(context, data, ID_RELEASE)
                    }
                }
            }

            Log.d(TAG, "onReceive alarm release")
        } else if (type_alarm == ID_DAILY) {
            val data = arrayListOf<NotificationItem>().apply {
                add(NotificationItem(1, "Daily Reminder", "Sob, Udah Cek Movie Hari Ini?"))
            }
            sendNotification(context, data, ID_DAILY)

            Log.d(TAG, "onReceive alarm daily")
        }
    }

    fun setRepeatingAlarm(context: Context, type: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(KEY_TYPE, type)

        var time: String = "00:00"
        if (type == ID_RELEASE) {
            // alarm release setiap jam 0800
            time = "08:00"
        } else if (type == ID_DAILY) {
            // alarm daily setiap jam 0700
            time = "07:00"
        }
        val timeArray = time.split(":".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, type, intent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.let {
            it.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            Log.d(TAG, "setRepeatingAlarm type = $type, time = $time")
        }
    }

    fun cancelAlarm(context: Context, type: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, type, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "cancelAlarm: Alarm Dibatalkan $type")
    }

    fun sendNotification(context: Context, items: ArrayList<NotificationItem>, type: Int) {


        // jika tidak ada data yang dikirim, ga usah proses lagi
        if (items.size < 1) return

        val notifManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_MAIN_ACTIVITY,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm")
        val currentDate = sdf.format(Date())

        val builder: NotificationCompat.Builder
        if (items.size < 2) {
            // notif cuma 1
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("${items[0].sender}")
                .setContentText(items[0].message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setGroup(GROUP_KEY_EMAILS)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            // notif lebih besar dari 1
            val inboxStyle = NotificationCompat.InboxStyle()
            for (i in items.indices) {
                inboxStyle.addLine(items[i].message)
            }
            inboxStyle.setBigContentTitle("${items.size} New Released")
                .setSummaryText("yay ! new release")

            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("${items.size} New Released")
                .setContentText("${items[0].sender}")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setGroup(GROUP_KEY_EMAILS)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        }

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            notifManager.createNotificationChannel(channel)

            builder.setChannelId(CHANNEL_ID)
        }

        val notification = builder.build()
        notifManager.notify(type, notification)
        Log.d(TAG, "sendNotification ${items.size} with $type")
    }
}

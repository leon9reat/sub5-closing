package com.medialink.sub5close.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.medialink.sub5close.Consts
import com.medialink.sub5close.R
import com.medialink.sub5close.database.Favorite
import com.medialink.sub5close.database.FavoriteDatabase
import com.medialink.sub5close.widget.Sub5Widget.Companion.TOAST_ACTION

/**
 * Implementation of App Widget functionality.
 */
class Sub5Widget : AppWidgetProvider() {
    companion object {
        private val TAG = Sub5Widget::class.java.simpleName

        const val TOAST_ACTION = "com.medialink.sub5close.widget.TOAST_ACTION"
        const val EXTRA_ITEM: String = "com.medialink.sub5close.widget.EXTRA_ITEM"
    }



    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        Log.d(TAG, "ON RECEIVE WIDGET ${intent?.action.toString()}")
        if (intent?.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                val data = intent.getStringExtra("PARCEL")
                Toast.makeText(context, "Touch View ${data}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "ON onUpdate WIDGET ")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(TAG, "ON onEnabled WIDGET")
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "ON onDisabled WIDGET")
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)

    val intent = Intent(context, StackWidgetService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.sub5_widget)
    views.setRemoteAdapter(R.id.stack_view, intent)
    views.setEmptyView(R.id.stack_view, R.id.empty_view)

    val toastIntent = Intent(context, Sub5Widget::class.java)
    toastIntent.action = TOAST_ACTION
    toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    toastIntent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

    val toastPending = PendingIntent.getBroadcast(
        context,
        0,
        toastIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setPendingIntentTemplate(R.id.stack_view, toastPending)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
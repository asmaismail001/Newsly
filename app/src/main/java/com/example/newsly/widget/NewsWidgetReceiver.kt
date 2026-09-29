package com.example.newsly.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * BroadcastReceiver responsible for managing the lifecycle of the Newsly Home Screen Widget.
 * The Android system delivers APPWIDGET_UPDATE and system broadcast events to this receiver.
 */
class NewsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NewsWidget()
}

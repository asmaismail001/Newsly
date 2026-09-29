package com.example.newsly.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

/**
 * Action callback executed when the user taps the Refresh button on the widget.
 * Triggers a recomposition and re-fetch of top news headlines by updating the widget instance.
 */
class RefreshActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Re-execute provideGlance() for this specific widget instance to fetch latest headlines
        NewsWidget().update(context, glanceId)
    }
}

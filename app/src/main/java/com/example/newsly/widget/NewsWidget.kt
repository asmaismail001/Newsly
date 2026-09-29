package com.example.newsly.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.newsly.MainActivity
import com.example.newsly.NewslyApplication
import com.example.newsly.data.model.NewsArticle

class NewsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Access repository through application container
        val app = context.applicationContext as? NewslyApplication
        val repository = app?.container?.newsRepository

        // Fetch top headlines for "All" category
        val articlesResult = try {
            repository?.getNewsByCategory("All")
        } catch (e: Exception) {
            Result.failure(e)
        }

        val articles = articlesResult?.getOrNull()?.take(4) ?: emptyList()
        val isError = articlesResult == null || articlesResult.isFailure || articles.isEmpty()

        provideContent {
            NewsWidgetContent(
                articles = articles,
                isError = isError
            )
        }
    }

    @Composable
    private fun NewsWidgetContent(
        articles: List<NewsArticle>,
        isError: Boolean
    ) {
        // Dark theme container (#141414)
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFF141414))
                .cornerRadius(16.dp)
                .padding(12.dp)
        ) {
            // Header Bar
            WidgetHeader()

            Spacer(modifier = GlanceModifier.height(8.dp))

            if (isError) {
                // Error / Empty fallback state
                WidgetErrorState()
            } else {
                // Top 4 Headlines list
                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight()
                ) {
                    articles.forEachIndexed { index, article ->
                        ArticleItemRow(
                            article = article,
                            isLast = index == articles.lastIndex
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun WidgetHeader() {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .clickable(actionStartActivity<MainActivity>()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App branding badge
            Box(
                modifier = GlanceModifier
                    .background(Color(0xFFFF3B30))
                    .cornerRadius(4.dp)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "NEWSLY",
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = GlanceModifier.width(8.dp))

            Text(
                text = "Top Headlines",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = GlanceModifier.defaultWeight()
            )

            // Refresh Button (triggers manual re-fetch)
            Box(
                modifier = GlanceModifier
                    .background(Color(0xFF242424))
                    .cornerRadius(8.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable(actionRunCallback<RefreshActionCallback>())
            ) {
                Text(
                    text = "⟳ Refresh",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF4DA6FF)),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }

    @Composable
    private fun ColumnScope.ArticleItemRow(
        article: NewsArticle,
        isLast: Boolean
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight()
                .clickable(actionStartActivity<MainActivity>())
                .padding(vertical = 2.dp)
        ) {
            // Headline title
            Text(
                text = article.title,
                maxLines = 2,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = GlanceModifier.height(2.dp))

            // Source & Published time subtitle
            val timeAgo = article.getFormattedTimeAgo()
            val metadata = if (article.sourceName.isNotBlank()) {
                "${article.sourceName} • $timeAgo"
            } else {
                timeAgo
            }

            Text(
                text = metadata,
                maxLines = 1,
                style = TextStyle(
                    color = ColorProvider(Color(0xFFA0A0A0)),
                    fontSize = 10.sp
                )
            )

            if (!isLast) {
                Spacer(modifier = GlanceModifier.height(2.dp))
                // Subtle divider between articles
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF252525))
                ) {}
            }
        }
    }

    @Composable
    private fun WidgetErrorState() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️ Couldn't load headlines",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = GlanceModifier.height(4.dp))

            Text(
                text = "Check connection or tap Refresh to retry",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF888888)),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = GlanceModifier.height(8.dp))

            Box(
                modifier = GlanceModifier
                    .background(Color(0xFF333333))
                    .cornerRadius(6.dp)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable(actionRunCallback<RefreshActionCallback>())
            ) {
                Text(
                    text = "Try Again",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF4DA6FF)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

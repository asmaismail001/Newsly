package com.example.newsly.ui.news.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsly.data.model.NewsArticle
import com.example.newsly.ui.theme.Indigo600
import com.example.newsly.ui.theme.PureWhite
import com.example.newsly.ui.theme.Slate200
import com.example.newsly.ui.theme.Slate400
import com.example.newsly.ui.theme.Slate500
import com.example.newsly.ui.theme.Slate700
import com.example.newsly.ui.theme.Slate900

@Composable
fun NewsCard(
    article: NewsArticle,
    onArticleClick: (NewsArticle) -> Unit,
    onBookmarkClick: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp), spotColor = Slate400.copy(alpha = 0.2f))
            .clip(RoundedCornerShape(16.dp))
            .background(PureWhite)
            .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(16.dp))
            .clickable { onArticleClick(article) }
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Optional Card Image Banner
            if (!article.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(article.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // Category tag
            Text(
                text = article.category.uppercase(),
                color = Indigo600,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                style = MaterialTheme.typography.titleSmall
            )

            // Headline (18-20sp SemiBold/Bold)
            Text(
                text = article.title,
                color = Slate900,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )

            // Short description (14-15sp Regular)
            if (!article.description.isNullOrBlank()) {
                Text(
                    text = article.description,
                    color = Slate700,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Footer: Source, Time, Bookmark, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Source & Time
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = article.sourceName,
                        color = Slate900,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "•",
                        color = Slate400,
                        fontSize = 12.sp
                    )
                    Text(
                        text = article.getFormattedTimeAgo(),
                        color = Slate500,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Share button
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, article.title)
                                putExtra(Intent.EXTRA_TEXT, "${article.title}\n\nRead more at: ${article.url}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share News"))
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = Slate500,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Bookmark button
                    IconButton(
                        onClick = { onBookmarkClick(article) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (article.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (article.isBookmarked) Indigo600 else Slate500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

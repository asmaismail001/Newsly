package com.example.newsly.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsly.data.model.NewsArticle
import com.example.newsly.data.model.NewsCategory
import com.example.newsly.data.repository.NewsRepository
import com.example.newsly.data.repository.NotificationRepository
import com.example.newsly.ui.news.components.FeaturedNewsCard
import com.example.newsly.ui.theme.CoralAccent
import com.example.newsly.ui.theme.CoralPrimary
import com.example.newsly.ui.theme.Indigo100
import com.example.newsly.ui.theme.Indigo50
import com.example.newsly.ui.theme.Indigo600
import com.example.newsly.ui.theme.Indigo700
import com.example.newsly.ui.theme.PureWhite
import com.example.newsly.ui.theme.Slate100
import com.example.newsly.ui.theme.Slate200
import com.example.newsly.ui.theme.Slate400
import com.example.newsly.ui.theme.Slate50
import com.example.newsly.ui.theme.Slate500
import com.example.newsly.ui.theme.Slate700
import com.example.newsly.ui.theme.Slate900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    newsRepository: NewsRepository,
    notificationRepository: NotificationRepository,
    onMenuClick: () -> Unit,
    onNotificationBellClick: () -> Unit,
    onNavigateToNews: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToTranslation: () -> Unit,
    onArticleClick: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    val savedArticles by newsRepository.getAllSavedArticles().collectAsState(initial = emptyList())
    val notifications by notificationRepository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Newsly Dashboard",
                            color = Slate900,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(
                            text = "Daily digest & global updates",
                            color = Slate500,
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Menu",
                            tint = Slate900
                        )
                    }
                },
                actions = {
                    Box(modifier = Modifier.padding(end = 6.dp)) {
                        IconButton(onClick = onNotificationBellClick) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Slate700
                            )
                        }
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 6.dp, end = 6.dp)
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(CoralAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$unreadCount",
                                    color = PureWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Slate50)
            )
        },
        containerColor = Slate50,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Quick Stat Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Saved News",
                    value = "${savedArticles.size}",
                    icon = Icons.Outlined.BookmarkBorder,
                    iconTint = Indigo700,
                    iconBg = Indigo50,
                    onClick = onNavigateToSaved,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Urdu Translate",
                    value = "Active",
                    icon = Icons.Outlined.Language,
                    iconTint = CoralPrimary,
                    iconBg = CoralPrimary.copy(alpha = 0.1f),
                    onClick = onNavigateToTranslation,
                    modifier = Modifier.weight(1f)
                )
            }

            // Quick Category Shortcuts
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Explore Topics",
                        color = Slate900,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    TextButton(onClick = onNavigateToNews) {
                        Text("View Feed", color = Indigo700, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Icon(Icons.Outlined.ChevronRight, null, tint = Indigo700, modifier = Modifier.size(16.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(
                        "Technology" to "💻",
                        "Business" to "📈",
                        "Science" to "🔬",
                        "Health" to "🩺",
                        "Sports" to "⚽",
                        "World" to "🌍",
                        "Politics" to "🏛️",
                        "Environment" to "🌱"
                    ).forEach { (category, emoji) ->
                        TopicShortcutChip(
                            emoji = emoji,
                            name = category,
                            onClick = { onNavigateToCategory(category) }
                        )
                    }
                }
            }

            // Featured Spotlight Card
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Spotlight Story",
                    color = Slate900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )

                val spotlightArticle = NewsArticle(
                    id = "art-breaking-1",
                    title = "Global AI Summit Unveils Next-Gen Autonomous Systems and Breakthrough Quantum Computing",
                    description = "World technology leaders gather in Geneva to establish ethical frameworks and showcase quantum architectures.",
                    content = "World technology leaders gathered today at the prestigious International AI and Quantum Summit in Geneva...",
                    url = "https://www.bbc.com/news/technology",
                    imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1200&q=80",
                    publishedAt = "2026-09-06T13:45:00Z",
                    sourceName = "BBC News",
                    author = "Eleanor Vance",
                    category = "Technology",
                    isBreaking = true
                )

                FeaturedNewsCard(
                    article = spotlightArticle,
                    onArticleClick = onArticleClick,
                    onBookmarkClick = { }
                )
            }

            // Recent Alerts Section
            if (notifications.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Alerts",
                            color = Slate900,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        TextButton(onClick = onNotificationBellClick) {
                            Text("See all", color = Indigo700, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    notifications.take(2).forEach { notif ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNotificationBellClick() }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Indigo50),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = null,
                                        tint = Indigo700,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = notif.title,
                                        color = Slate900,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = notif.getFormattedTimeAgo(),
                                        color = Slate400,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = value,
                    color = Slate900,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    color = Slate500,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun TopicShortcutChip(
    emoji: String,
    name: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PureWhite)
            .border(1.dp, Slate200, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Text(
                text = name,
                color = Slate900,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

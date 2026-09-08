package com.example.newsly.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsly.ui.theme.CoralAccent
import com.example.newsly.ui.theme.Indigo100
import com.example.newsly.ui.theme.Indigo700
import com.example.newsly.ui.theme.PureWhite
import com.example.newsly.ui.theme.Slate400
import com.example.newsly.ui.theme.Slate700

data class BottomBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun AppBottomBar(
    currentRoute: String,
    unreadNotificationCount: Int,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomBarItem(Screen.Dashboard.route, "Dashboard", Icons.Outlined.Dashboard),
        BottomBarItem(Screen.NewsFeed.route, "News", Icons.Outlined.Newspaper),
        BottomBarItem(Screen.SavedNews.route, "Saved", Icons.Outlined.BookmarkBorder),
        BottomBarItem(Screen.Notifications.route, "Alerts", Icons.Outlined.Notifications, badgeCount = unreadNotificationCount),
        BottomBarItem(Screen.NotificationSettings.route, "Settings", Icons.Outlined.Settings)
    )

    NavigationBar(
        modifier = modifier.shadow(elevation = 12.dp),
        containerColor = PureWhite,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateTo(item.route) },
                icon = {
                    Box {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) Indigo700 else Slate400,
                            modifier = Modifier.size(24.dp)
                        )
                        if (item.badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(CoralAccent)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) Indigo700 else Slate400,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Indigo100.copy(alpha = 0.5f)
                )
            )
        }
    }
}

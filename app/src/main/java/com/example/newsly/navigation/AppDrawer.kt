package com.example.newsly.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsly.ui.theme.CoralAccent
import com.example.newsly.ui.theme.Indigo100
import com.example.newsly.ui.theme.Indigo50
import com.example.newsly.ui.theme.Indigo700
import com.example.newsly.ui.theme.PureWhite
import com.example.newsly.ui.theme.Slate200
import com.example.newsly.ui.theme.Slate400
import com.example.newsly.ui.theme.Slate500
import com.example.newsly.ui.theme.Slate700
import com.example.newsly.ui.theme.Slate900

data class DrawerMenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun AppDrawer(
    currentRoute: String,
    unreadNotificationCount: Int,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainItems = listOf(
        DrawerMenuItem(Screen.Dashboard.route, "Dashboard", Icons.Outlined.Dashboard),
        DrawerMenuItem(Screen.NewsFeed.route, "News Feed", Icons.Outlined.Newspaper)
    )

    val newsItems = listOf(
        DrawerMenuItem(Screen.Notifications.route, "Notifications", Icons.Outlined.Notifications, badgeCount = unreadNotificationCount),
        DrawerMenuItem(Screen.SavedNews.route, "Saved News", Icons.Outlined.BookmarkBorder),
        DrawerMenuItem(Screen.Translation.route, "Urdu Translation", Icons.Outlined.Language)
    )

    val appItems = listOf(
        DrawerMenuItem(Screen.Settings.route, "Settings", Icons.Outlined.Settings),
        DrawerMenuItem(Screen.Profile.route, "Profile", Icons.Outlined.PersonOutline)
    )

    val scrollState = rememberScrollState()

    ModalDrawerSheet(
        modifier = modifier.width(280.dp), // Compact width 280dp as requested
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(vertical = 20.dp, horizontal = 14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header Logo & Branding
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Indigo700),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "N",
                            color = PureWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Newsly",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CoralAccent)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "PRO",
                                    color = PureWhite,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Stay informed. Stay ahead.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Section 1: MAIN
                SectionHeader(title = "MAIN")
                mainItems.forEach { item ->
                    DrawerItemRow(
                        item = item,
                        isSelected = currentRoute == item.route,
                        onClick = { onNavigateTo(item.route) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Section 2: NEWS
                SectionHeader(title = "NEWS")
                newsItems.forEach { item ->
                    DrawerItemRow(
                        item = item,
                        isSelected = currentRoute == item.route,
                        onClick = { onNavigateTo(item.route) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Section 3: APP
                SectionHeader(title = "APP")
                appItems.forEach { item ->
                    DrawerItemRow(
                        item = item,
                        isSelected = currentRoute == item.route,
                        onClick = { onNavigateTo(item.route) }
                    )
                }
            }

            // Footer Version
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Newsly v1.0.0",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Jetpack Compose • Material 3",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Composable
private fun DrawerItemRow(
    item: DrawerMenuItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (isSelected) Indigo700 else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        },
        label = {
            Text(
                text = item.title,
                color = if (isSelected) Indigo700 else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        badge = {
            if (item.badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CoralAccent)
                        .padding(horizontal = 7.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "${item.badgeCount}",
                        color = PureWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        selected = isSelected,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Indigo50,
            unselectedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

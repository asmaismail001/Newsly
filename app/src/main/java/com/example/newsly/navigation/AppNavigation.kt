package com.example.newsly.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsly.NewslyApplication
import com.example.newsly.data.model.NewsArticle
import com.example.newsly.data.model.NewsCategory
import com.example.newsly.di.AppContainer
import com.example.newsly.ui.dashboard.DashboardScreen
import com.example.newsly.ui.news.NewsDetailScreen
import com.example.newsly.ui.news.NewsScreen
import com.example.newsly.ui.news.NewsViewModel
import com.example.newsly.ui.notifications.NotificationSettingsScreen
import com.example.newsly.ui.notifications.NotificationsScreen
import com.example.newsly.ui.notifications.RightSideNotificationPanel
import com.example.newsly.ui.profile.ProfileScreen
import com.example.newsly.ui.saved.SavedNewsScreen
import com.example.newsly.ui.settings.SettingsScreen
import com.example.newsly.ui.translation.TranslationScreen
import com.example.newsly.ui.translation.TranslationViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object NewsFeed : Screen("news_feed")
    data object NewsDetail : Screen("news_detail")
    data object SavedNews : Screen("saved_news")
    data object Notifications : Screen("notifications")
    data object NotificationSettings : Screen("notification_settings")
    data object Translation : Screen("translation")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isRightPanelOpen by remember { mutableStateOf(false) }

    // Shared selected article for detail view
    var selectedArticle by remember { mutableStateOf<NewsArticle?>(null) }

    val newsViewModel: NewsViewModel = viewModel(
        factory = NewsViewModel.provideFactory(appContainer.newsRepository)
    )

    val translationViewModel: TranslationViewModel = viewModel(
        factory = TranslationViewModel.provideFactory(appContainer.translationRepository)
    )

    val notifications by appContainer.notificationRepository.notifications.collectAsState()
    val unreadNotificationCount = notifications.count { !it.isRead }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.NewsFeed.route

    val shouldShowBottomBar = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.NewsFeed.route,
        Screen.SavedNews.route,
        Screen.Notifications.route,
        Screen.NotificationSettings.route
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                unreadNotificationCount = unreadNotificationCount,
                onNavigateTo = { route ->
                    coroutineScope.launch { drawerState.close() }
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Screen.NewsFeed.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                bottomBar = {
                    if (shouldShowBottomBar) {
                        AppBottomBar(
                            currentRoute = currentRoute,
                            unreadNotificationCount = unreadNotificationCount,
                            onNavigateTo = { route ->
                                if (currentRoute != route) {
                                    navController.navigate(route) {
                                        popUpTo(Screen.NewsFeed.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.NewsFeed.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.NewsFeed.route) {
                        NewsScreen(
                            viewModel = newsViewModel,
                            unreadNotificationCount = unreadNotificationCount,
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            onNotificationBellClick = { isRightPanelOpen = true },
                            onArticleClick = { article ->
                                selectedArticle = article
                                navController.navigate(Screen.NewsDetail.route)
                            }
                        )
                    }

                    composable(Screen.Dashboard.route) {
                        DashboardScreen(
                            newsRepository = appContainer.newsRepository,
                            notificationRepository = appContainer.notificationRepository,
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            onNotificationBellClick = { isRightPanelOpen = true },
                            onNavigateToNews = {
                                navController.navigate(Screen.NewsFeed.route)
                            },
                            onNavigateToCategory = { categoryName ->
                                newsViewModel.selectCategory(categoryName)
                                navController.navigate(Screen.NewsFeed.route)
                            },
                            onNavigateToSaved = {
                                navController.navigate(Screen.SavedNews.route)
                            },
                            onNavigateToTranslation = {
                                navController.navigate(Screen.Translation.route)
                            },
                            onArticleClick = { article ->
                                selectedArticle = article
                                navController.navigate(Screen.NewsDetail.route)
                            }
                        )
                    }

                    composable(Screen.NewsDetail.route) {
                        selectedArticle?.let { article ->
                            NewsDetailScreen(
                                article = article,
                                onBackClick = { navController.popBackStack() },
                                onBookmarkToggle = { newsViewModel.toggleBookmark(it) },
                                translationViewModel = translationViewModel
                            )
                        } ?: run {
                            // Fallback if empty
                            navController.popBackStack()
                        }
                    }

                    composable(Screen.SavedNews.route) {
                        SavedNewsScreen(
                            newsRepository = appContainer.newsRepository,
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            onArticleClick = { article ->
                                selectedArticle = article
                                navController.navigate(Screen.NewsDetail.route)
                            }
                        )
                    }

                    composable(Screen.Notifications.route) {
                        NotificationsScreen(
                            notifications = notifications,
                            onBackClick = { navController.popBackStack() },
                            onSettingsClick = {
                                navController.navigate(Screen.NotificationSettings.route)
                            },
                            onNotificationClick = { notif ->
                                appContainer.notificationRepository.markAsRead(notif.id)
                                notif.articleId?.let { artId ->
                                    coroutineScope.launch {
                                        val art = appContainer.newsRepository.getArticleById(artId)
                                        if (art != null) {
                                            selectedArticle = art
                                            navController.navigate(Screen.NewsDetail.route)
                                        }
                                    }
                                }
                            },
                            onMarkAllAsRead = { appContainer.notificationRepository.markAllAsRead() },
                            onClearAll = { appContainer.notificationRepository.clearNotifications() }
                        )
                    }

                    composable(Screen.NotificationSettings.route) {
                        NotificationSettingsScreen(
                            notificationRepository = appContainer.notificationRepository,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.Translation.route) {
                        TranslationScreen(
                            viewModel = translationViewModel,
                            onMenuClick = { coroutineScope.launch { drawerState.open() } }
                        )
                    }

                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            onMenuClick = { coroutineScope.launch { drawerState.open() } }
                        )
                    }

                    composable(Screen.Settings.route) {
                        SettingsScreen(
                            themeManager = appContainer.themeManager,
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            onNavigateToNotificationSettings = {
                                navController.navigate(Screen.NotificationSettings.route)
                            },
                            onNavigateToTranslation = {
                                navController.navigate(Screen.Translation.route)
                            }
                        )
                    }
                }
            }

            // Right-side Slide-in Notification Panel Overlay
            RightSideNotificationPanel(
                isOpen = isRightPanelOpen,
                notifications = notifications,
                onClose = { isRightPanelOpen = false },
                onNotificationClick = { notif ->
                    appContainer.notificationRepository.markAsRead(notif.id)
                    notif.articleId?.let { artId ->
                        coroutineScope.launch {
                            val art = appContainer.newsRepository.getArticleById(artId)
                            if (art != null) {
                                isRightPanelOpen = false
                                selectedArticle = art
                                navController.navigate(Screen.NewsDetail.route)
                            }
                        }
                    }
                },
                onMarkAllAsRead = { appContainer.notificationRepository.markAllAsRead() },
                onClearAll = { appContainer.notificationRepository.clearNotifications() }
            )
        }
    }
}

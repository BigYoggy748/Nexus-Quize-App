package com.nexus.quiz.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.nexus.quiz.data.local.PreferencesManager
import com.nexus.quiz.data.repository.NexusRepository
import com.nexus.quiz.engine.GeminiService
import com.nexus.quiz.ui.components.NexusBottomBar
import com.nexus.quiz.ui.screens.AnalyticsScreen
import com.nexus.quiz.ui.screens.DeckDetailScreen
import com.nexus.quiz.ui.screens.HomeScreen
import com.nexus.quiz.ui.screens.IngestionScreen
import com.nexus.quiz.ui.screens.OnboardingScreen
import com.nexus.quiz.ui.screens.ReviewSessionScreen
import com.nexus.quiz.ui.screens.SettingsScreen
import com.nexus.quiz.ui.theme.White
import com.nexus.quiz.ui.viewmodel.AnalyticsViewModel
import com.nexus.quiz.ui.viewmodel.DeckDetailViewModel
import com.nexus.quiz.ui.viewmodel.HomeViewModel
import com.nexus.quiz.ui.viewmodel.IngestionViewModel
import com.nexus.quiz.ui.viewmodel.ReviewViewModel
import com.nexus.quiz.ui.viewmodel.SettingsViewModel
import com.nexus.quiz.ui.viewmodel.ViewModelFactory

@Composable
fun NexusNavGraph(
    navController: NavHostController,
    repository: NexusRepository,
    preferencesManager: PreferencesManager,
    geminiService: GeminiService
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val isOnboarded = preferencesManager.isOnboarded()
    val startDestination = if (isOnboarded) Screen.Home.route else Screen.Onboarding.route

    val username by preferencesManager.usernameFlow.collectAsState()

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Ingestion.route,
        Screen.Analytics.route,
        Screen.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NexusBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        containerColor = White
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Onboarding
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onComplete = { enteredName ->
                        preferencesManager.setUsername(enteredName)
                        preferencesManager.setOnboarded(true)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // Home / Decks
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = ViewModelFactory(repository, preferencesManager, geminiService)
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToReview = { deckId ->
                        navController.navigate(Screen.ReviewSession.createRoute(deckId))
                    },
                    onNavigateToIngestion = {
                        navController.navigate(Screen.Ingestion.route)
                    },
                    onNavigateToDeckDetail = { deckId ->
                        navController.navigate(Screen.DeckDetail.createRoute(deckId))
                    },
                    username = username
                )
            }

            // Ingestion / Ask AI
            composable(Screen.Ingestion.route) {
                val ingestionViewModel: IngestionViewModel = viewModel(
                    factory = ViewModelFactory(repository, preferencesManager, geminiService)
                )
                IngestionScreen(
                    viewModel = ingestionViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onSavedSuccessfully = { navController.navigate(Screen.Home.route) }
                )
            }

            // Analytics
            composable(Screen.Analytics.route) {
                val analyticsViewModel: AnalyticsViewModel = viewModel(
                    factory = ViewModelFactory(repository, preferencesManager, geminiService)
                )
                AnalyticsScreen(viewModel = analyticsViewModel)
            }

            // Settings
            composable(Screen.Settings.route) {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = ViewModelFactory(repository, preferencesManager, geminiService)
                )
                SettingsScreen(viewModel = settingsViewModel)
            }

            // Deck Detail
            composable(
                route = Screen.DeckDetail.route,
                arguments = listOf(navArgument("deckId") { type = NavType.LongType })
            ) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getLong("deckId") ?: -1L
                val deckDetailViewModel: DeckDetailViewModel = viewModel(
                    factory = ViewModelFactory(repository, preferencesManager, geminiService, extraId = deckId)
                )
                DeckDetailScreen(
                    viewModel = deckDetailViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onStartReview = {
                        navController.navigate(Screen.ReviewSession.createRoute(deckId))
                    }
                )
            }

            // Review Session
            composable(
                route = Screen.ReviewSession.route,
                arguments = listOf(navArgument("deckId") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getLong("deckId")?.takeIf { it != -1L }
                val reviewViewModel: ReviewViewModel = viewModel(
                    factory = ViewModelFactory(repository, preferencesManager, geminiService)
                )
                ReviewSessionScreen(
                    viewModel = reviewViewModel,
                    deckId = deckId,
                    onFinishSession = { navController.popBackStack() }
                )
            }
        }
    }
}

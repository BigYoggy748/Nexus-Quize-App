package com.nexus.quiz.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Ingestion : Screen("ingestion")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
    object DeckDetail : Screen("deck_detail/{deckId}") {
        fun createRoute(deckId: Long) = "deck_detail/$deckId"
    }
    object ReviewSession : Screen("review_session?deckId={deckId}") {
        fun createRoute(deckId: Long? = null) = if (deckId != null) "review_session?deckId=$deckId" else "review_session"
    }
}

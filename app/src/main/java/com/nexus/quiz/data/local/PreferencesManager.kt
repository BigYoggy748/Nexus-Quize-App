package com.nexus.quiz.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "nexus_user_preferences",
        Context.MODE_PRIVATE
    )

    private val _apiKeyFlow = MutableStateFlow(getApiKey())
    val apiKeyFlow: StateFlow<String> = _apiKeyFlow.asStateFlow()

    private val _modelFlow = MutableStateFlow(getSelectedModel())
    val modelFlow: StateFlow<String> = _modelFlow.asStateFlow()

    private val _usernameFlow = MutableStateFlow(getUsername())
    val usernameFlow: StateFlow<String> = _usernameFlow.asStateFlow()

    init {
        // Auto-migrate legacy/deprecated models (such as gemini-2.0-flash or gemini-1.5-*)
        val current = prefs.getString(KEY_MODEL, null)
        if (current != null && (isDeprecatedModel(current) || AVAILABLE_MODELS.none { it.first == current })) {
            prefs.edit().putString(KEY_MODEL, DEFAULT_MODEL).apply()
            _modelFlow.value = DEFAULT_MODEL
        }
    }

    // ── API Key ──

    fun getApiKey(): String {
        return prefs.getString(KEY_API_KEY, "") ?: ""
    }

    fun setApiKey(apiKey: String) {
        prefs.edit().putString(KEY_API_KEY, apiKey.trim()).apply()
        _apiKeyFlow.value = apiKey.trim()
    }

    // ── Model Selection ──

    fun getSelectedModel(): String {
        val stored = prefs.getString(KEY_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL
        if (isDeprecatedModel(stored) || AVAILABLE_MODELS.none { it.first == stored }) {
            prefs.edit().putString(KEY_MODEL, DEFAULT_MODEL).apply()
            return DEFAULT_MODEL
        }
        return stored
    }

    fun setSelectedModel(model: String) {
        prefs.edit().putString(KEY_MODEL, model).apply()
        _modelFlow.value = model
    }

    // ── Daily Goal ──

    fun getDailyGoal(): Int {
        return prefs.getInt(KEY_DAILY_GOAL, 25)
    }

    fun setDailyGoal(goal: Int) {
        prefs.edit().putInt(KEY_DAILY_GOAL, goal).apply()
    }

    // ── Username & Onboarding ──

    fun getUsername(): String {
        return prefs.getString(KEY_USERNAME, "") ?: ""
    }

    fun setUsername(username: String) {
        prefs.edit().putString(KEY_USERNAME, username.trim()).apply()
        _usernameFlow.value = username.trim()
    }

    fun isOnboarded(): Boolean {
        return prefs.getBoolean(KEY_IS_ONBOARDED, false)
    }

    fun setOnboarded(onboarded: Boolean) {
        prefs.edit().putBoolean(KEY_IS_ONBOARDED, onboarded).apply()
    }

    fun getAvatarInitials(): String {
        val name = getUsername()
        if (name.isBlank()) return "N"
        val parts = name.trim().split("\\s+".toRegex())
        return if (parts.size >= 2) {
            "${parts.first().first().uppercaseChar()}${parts.last().first().uppercaseChar()}"
        } else {
            name.first().uppercaseChar().toString()
        }
    }

    companion object {
        private const val KEY_API_KEY = "gemini_api_key"
        private const val KEY_MODEL = "gemini_model"
        private const val KEY_DAILY_GOAL = "daily_review_goal"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_ONBOARDED = "is_onboarded"

        const val DEFAULT_MODEL = "gemini-3.6-flash"
        val AVAILABLE_MODELS = listOf(
            "gemini-3.6-flash" to "Gemini 3.6 Flash (Fast & Efficient - Recommended)",
            "gemini-3.8-flash" to "Gemini 3.8 Flash (Next-Gen High Speed)",
            "gemini-3.1-pro" to "Gemini 3.1 Pro (Deep Technical Reasoning)"
        )

        fun isDeprecatedModel(model: String): Boolean {
            return model.isBlank() ||
                model.startsWith("gemini-2.") ||
                model.startsWith("gemini-1.") ||
                model == "gemini-pro"
        }
    }
}

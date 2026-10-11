package com.nexus.quiz

import android.app.Application
import com.nexus.quiz.data.local.NexusDatabase
import com.nexus.quiz.data.local.PreferencesManager
import com.nexus.quiz.data.repository.NexusRepository
import com.nexus.quiz.engine.GeminiService

class NexusApplication : Application() {
    val database by lazy { NexusDatabase.getDatabase(this) }
    val repository by lazy { NexusRepository(database) }
    val preferencesManager by lazy { PreferencesManager(this) }
    val geminiService by lazy {
        GeminiService(
            apiKeyProvider = { preferencesManager.getApiKey() },
            modelProvider = { preferencesManager.getSelectedModel() }
        )
    }
}

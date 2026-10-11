package com.nexus.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nexus.quiz.ui.navigation.NexusNavGraph
import com.nexus.quiz.ui.theme.NexusTheme
import com.nexus.quiz.ui.theme.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as NexusApplication

        setContent {
            NexusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = White
                ) {
                    val navController = rememberNavController()
                    NexusNavGraph(
                        navController = navController,
                        repository = app.repository,
                        preferencesManager = app.preferencesManager,
                        geminiService = app.geminiService
                    )
                }
            }
        }
    }
}

package com.nexus.quiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.quiz.data.local.dao.RetentionOverview
import com.nexus.quiz.data.local.dao.ReviewAccuracyStats
import com.nexus.quiz.data.local.entity.ReviewLogEntity
import com.nexus.quiz.data.repository.NexusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AnalyticsViewModel(private val repository: NexusRepository) : ViewModel() {

    val retentionOverview: StateFlow<RetentionOverview> = repository.getRetentionOverview()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RetentionOverview(0, 0, 0, 0, 0)
        )

    val accuracyStats: StateFlow<ReviewAccuracyStats> = repository.getAccuracyStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReviewAccuracyStats(0, 0, 0, 0, 0)
        )

    val recentLogs: StateFlow<List<ReviewLogEntity>> = repository.getRecentLogs(50)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

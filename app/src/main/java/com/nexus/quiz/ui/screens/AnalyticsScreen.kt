package com.nexus.quiz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.data.local.entity.ReviewLogEntity
import com.nexus.quiz.data.model.SrsRating
import com.nexus.quiz.ui.components.NexusTopAppBar
import com.nexus.quiz.ui.theme.AccentBlue
import com.nexus.quiz.ui.theme.LightGray
import com.nexus.quiz.ui.theme.RatingAgain
import com.nexus.quiz.ui.theme.RatingEasy
import com.nexus.quiz.ui.theme.RatingGood
import com.nexus.quiz.ui.theme.RatingHard
import com.nexus.quiz.ui.theme.SystemGreen
import com.nexus.quiz.ui.theme.SystemOrange
import com.nexus.quiz.ui.theme.TextBlack
import com.nexus.quiz.ui.theme.TextPrimary
import com.nexus.quiz.ui.theme.TextSecondary
import com.nexus.quiz.ui.theme.TextTertiary
import com.nexus.quiz.ui.theme.White
import com.nexus.quiz.ui.viewmodel.AnalyticsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel) {
    val retention by viewModel.retentionOverview.collectAsState()
    val accuracy by viewModel.accuracyStats.collectAsState()
    val recentLogs by viewModel.recentLogs.collectAsState()

    Scaffold(
        topBar = { NexusTopAppBar(title = "Analytics") },
        containerColor = White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Retention Pipeline Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = White,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = TextBlack, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retention Pipeline", fontWeight = FontWeight.Bold, color = TextBlack, fontSize = 17.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val total = maxOf(1, retention.totalCards)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            RetentionBadge("Mature", "${retention.matureCards}", SystemGreen)
                            RetentionBadge("Young", "${retention.youngCards}", AccentBlue)
                            RetentionBadge("Learning", "${retention.learningCards}", SystemOrange)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { retention.matureCards.toFloat() / total },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = SystemGreen,
                            trackColor = LightGray
                        )
                    }
                }
            }

            // Rating Distribution Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = White,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.BarChart, contentDescription = null, tint = TextBlack, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Rating Distribution", fontWeight = FontWeight.Bold, color = TextBlack, fontSize = 17.sp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val totalReviews = maxOf(1, accuracy.totalReviews)

                        AccuracyBar("Easy", accuracy.easyCount, totalReviews, RatingEasy)
                        Spacer(modifier = Modifier.height(10.dp))
                        AccuracyBar("Good", accuracy.goodCount, totalReviews, RatingGood)
                        Spacer(modifier = Modifier.height(10.dp))
                        AccuracyBar("Hard", accuracy.hardCount, totalReviews, RatingHard)
                        Spacer(modifier = Modifier.height(10.dp))
                        AccuracyBar("Again", accuracy.againCount, totalReviews, RatingAgain)
                    }
                }
            }

            // Recent Logs Header
            item {
                Text(
                    text = "Recent Reviews",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
            }

            if (recentLogs.isEmpty()) {
                item {
                    Text(
                        "Complete a study session to see your review history.",
                        fontSize = 14.sp, color = TextTertiary
                    )
                }
            } else {
                items(recentLogs, key = { it.id }) { log ->
                    ReviewLogItem(log)
                }
            }

            item { Spacer(modifier = Modifier.height(36.dp)) }
        }
    }
}

@Composable
private fun RetentionBadge(label: String, countText: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = countText, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        Text(text = label, fontSize = 12.sp, color = TextTertiary)
    }
}

@Composable
private fun AccuracyBar(label: String, count: Int, total: Int, color: Color) {
    val pct = (count * 100) / total
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text("$count ($pct%)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { count.toFloat() / total },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = LightGray
        )
    }
}

@Composable
private fun ReviewLogItem(log: ReviewLogEntity) {
    val timeFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val ratingColor = when (log.rating) {
        SrsRating.AGAIN -> RatingAgain
        SrsRating.HARD -> RatingHard
        SrsRating.GOOD -> RatingGood
        SrsRating.EASY -> RatingEasy
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = LightGray
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ratingColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = log.rating.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ratingColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${log.intervalBefore}d → ${log.intervalAfter}d",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Text(
                        text = "EF: ${"%.2f".format(log.easeFactorBefore)} → ${"%.2f".format(log.easeFactorAfter)}",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }
            }

            Text(
                text = timeFormat.format(Date(log.reviewTimestamp)),
                fontSize = 11.sp,
                color = TextTertiary
            )
        }
    }
}

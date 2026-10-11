package com.nexus.quiz.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.data.model.FeynmanAuditResponseDto
import com.nexus.quiz.data.model.SrsRating
import com.nexus.quiz.engine.LatexParser
import com.nexus.quiz.ui.components.MathView
import com.nexus.quiz.ui.components.NexusTopAppBar
import com.nexus.quiz.ui.components.RatingBar
import com.nexus.quiz.ui.theme.AccentBlue
import com.nexus.quiz.ui.theme.BorderLight
import com.nexus.quiz.ui.theme.ForestGreen
import com.nexus.quiz.ui.theme.LightGray
import com.nexus.quiz.ui.theme.SageGreen
import com.nexus.quiz.ui.theme.SystemGreen
import com.nexus.quiz.ui.theme.SystemIndigo
import com.nexus.quiz.ui.theme.SystemOrange
import com.nexus.quiz.ui.theme.SystemRed
import com.nexus.quiz.ui.theme.TextBlack
import com.nexus.quiz.ui.theme.TextPrimary
import com.nexus.quiz.ui.theme.TextSecondary
import com.nexus.quiz.ui.theme.TextTertiary
import com.nexus.quiz.ui.theme.WarmParchment
import com.nexus.quiz.ui.theme.White
import com.nexus.quiz.ui.viewmodel.ReviewUiState
import com.nexus.quiz.ui.viewmodel.ReviewViewModel

@Composable
fun ReviewSessionScreen(
    viewModel: ReviewViewModel,
    deckId: Long? = null,
    onFinishSession: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(deckId) {
        viewModel.startSession(deckId)
    }

    Scaffold(
        topBar = {
            NexusTopAppBar(
                title = "Review",
                showBackButton = true,
                onBackClick = onFinishSession
            )
        },
        containerColor = White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ReviewUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TextBlack)
                    }
                }
                is ReviewUiState.Empty -> {
                    EmptyReviewState(onBackClick = onFinishSession)
                }
                is ReviewUiState.Active -> {
                    ActiveReviewContent(
                        state = state,
                        onReveal = { viewModel.revealAnswer() },
                        onUpdateFeynman = { viewModel.updateFeynmanText(it) },
                        onAuditFeynman = { viewModel.auditFeynmanExplanation() },
                        onRateCard = { viewModel.rateCard(it) }
                    )
                }
                is ReviewUiState.Complete -> {
                    CompleteSessionState(
                        state = state,
                        onDone = onFinishSession
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ActiveReviewContent(
    state: ReviewUiState.Active,
    onReveal: () -> Unit,
    onUpdateFeynman: (String) -> Unit,
    onAuditFeynman: () -> Unit,
    onRateCard: (SrsRating) -> Unit
) {
    val card = state.currentCard
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = LightGray
            ) {
                Text(
                    text = card.type.displayName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Text(
                text = "${state.cardIndex + 1} of ${state.totalCards}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextTertiary
            )
        }

        LinearProgressIndicator(
            progress = { (state.cardIndex + 1).toFloat() / state.totalCards },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = TextBlack,
            trackColor = LightGray
        )

        // Question Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = White,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "QUESTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                MathView(
                    text = card.prompt,
                    latexFormula = card.latexFormula,
                    isRevealed = state.isAnswerRevealed,
                    minHeightDp = 50
                )
            }
        }

        // Feynman Section
        if (card.type == CardType.FEYNMAN_CONCEPT) {
            FeynmanSection(
                userText = state.feynmanUserText,
                onUpdateText = onUpdateFeynman,
                isLoading = state.feynmanAuditLoading,
                auditResult = state.feynmanAuditResult,
                isRevealed = state.isAnswerRevealed,
                onAuditClick = onAuditFeynman
            )
        }

        // Reveal Button
        if (!state.isAnswerRevealed) {
            Button(
                onClick = onReveal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextBlack,
                    contentColor = White
                )
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (card.type == CardType.CLOZE_FORMULA) "Reveal Answer" else "Show Answer",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }

        // Answer Section
        AnimatedVisibility(
            visible = state.isAnswerRevealed,
            enter = fadeIn() + slideInVertically { it / 4 }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = SageGreen.copy(alpha = 0.08f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmParchment)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "ANSWER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val formattedAnswer = LatexParser.ensureMathDelimiters(card.answer)
                        MathView(
                            text = formattedAnswer,
                            isRevealed = true,
                            minHeightDp = 32
                        )

                        if (card.explanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            MathView(
                                text = card.explanation,
                                isRevealed = true,
                                minHeightDp = 24
                            )
                        }
                    }
                }

                val suggestedRating = state.feynmanAuditResult?.suggestedRating?.let {
                    try { SrsRating.valueOf(it) } catch (e: Exception) { null }
                }

                RatingBar(
                    card = card,
                    suggestedRating = suggestedRating,
                    onRatingSelected = onRateCard
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeynmanSection(
    userText: String,
    onUpdateText: (String) -> Unit,
    isLoading: Boolean,
    auditResult: FeynmanAuditResponseDto?,
    isRevealed: Boolean,
    onAuditClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SystemIndigo.copy(alpha = 0.04f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = SystemIndigo,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Explain in your own words",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = userText,
                onValueChange = onUpdateText,
                placeholder = { Text("Describe the mechanism...", color = TextTertiary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !isRevealed || auditResult == null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SystemIndigo,
                    unfocusedBorderColor = BorderLight,
                    focusedTextColor = TextBlack,
                    unfocusedTextColor = TextBlack
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (auditResult == null) {
                Button(
                    onClick = onAuditClick,
                    enabled = userText.isNotBlank() && !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SystemIndigo,
                        contentColor = White
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Auditing...", fontSize = 13.sp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Audit with AI", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AI Audit", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (auditResult.score >= 70) SystemGreen.copy(alpha = 0.1f) else SystemOrange.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${auditResult.score}/100",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (auditResult.score >= 70) SystemGreen else SystemOrange,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(auditResult.critique, fontSize = 14.sp, color = TextPrimary, lineHeight = 20.sp)

                    if (auditResult.terminologyMatched.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Matched", fontSize = 12.sp, color = SystemGreen, fontWeight = FontWeight.SemiBold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            auditResult.terminologyMatched.forEach { term ->
                                Surface(shape = RoundedCornerShape(4.dp), color = SystemGreen.copy(alpha = 0.08f)) {
                                    Text("✓ $term", fontSize = 11.sp, color = SystemGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }

                    if (auditResult.terminologyMissing.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Missing", fontSize = 12.sp, color = SystemRed, fontWeight = FontWeight.SemiBold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            auditResult.terminologyMissing.forEach { term ->
                                Surface(shape = RoundedCornerShape(4.dp), color = SystemRed.copy(alpha = 0.08f)) {
                                    Text("✗ $term", fontSize = 11.sp, color = SystemRed, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyReviewState(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SystemGreen,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No Cards Due", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "All cards are scheduled for future review.",
                fontSize = 15.sp, color = TextTertiary, textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = TextBlack, contentColor = White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Go Back", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CompleteSessionState(
    state: ReviewUiState.Complete,
    onDone: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = SystemOrange,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Session Complete",
                fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextBlack
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Memory intervals recalibrated.",
                fontSize = 15.sp, color = TextTertiary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = LightGray
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Summary (${state.reviewedCount} cards)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                    Spacer(modifier = Modifier.height(14.dp))
                    SummaryRow("Easy", state.easyCount, SystemGreen)
                    Spacer(modifier = Modifier.height(6.dp))
                    SummaryRow("Good", state.goodCount, AccentBlue)
                    Spacer(modifier = Modifier.height(6.dp))
                    SummaryRow("Hard", state.hardCount, SystemOrange)
                    Spacer(modifier = Modifier.height(6.dp))
                    SummaryRow("Again", state.againCount, SystemRed)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TextBlack, contentColor = White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Done", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, count: Int, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = color)
        Text("$count", fontWeight = FontWeight.Bold, color = color, fontSize = 14.sp)
    }
}

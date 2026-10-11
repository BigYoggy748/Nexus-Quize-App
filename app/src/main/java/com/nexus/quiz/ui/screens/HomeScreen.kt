package com.nexus.quiz.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.data.local.dao.DeckWithStats
import com.nexus.quiz.data.model.DeckCategory
import com.nexus.quiz.ui.components.NexusTopAppBar
import com.nexus.quiz.ui.components.StatCard
import com.nexus.quiz.ui.theme.AccentBlue
import com.nexus.quiz.ui.theme.BorderLight
import com.nexus.quiz.ui.theme.LightGray
import com.nexus.quiz.ui.theme.SystemRed
import com.nexus.quiz.ui.theme.TextBlack
import com.nexus.quiz.ui.theme.TextSecondary
import com.nexus.quiz.ui.theme.TextTertiary
import com.nexus.quiz.ui.theme.White
import com.nexus.quiz.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToReview: (Long?) -> Unit,
    onNavigateToIngestion: () -> Unit,
    onNavigateToDeckDetail: (Long) -> Unit,
    username: String = ""
) {
    val decks by viewModel.decks.collectAsState()
    val dueCount by viewModel.dueCardsCount.collectAsState()
    val retention by viewModel.retentionOverview.collectAsState()
    val reviewsToday by viewModel.reviewsTodayCount.collectAsState()

    var showCreateDeckDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NexusTopAppBar(
                title = "Nexus",
                actions = {
                    IconButton(onClick = { showCreateDeckDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Deck",
                            tint = TextBlack
                        )
                    }
                }
            )
        },
        containerColor = White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Personalized Greeting Header
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Avatar Initials
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = if (username.isNotBlank()) {
                            val parts = username.trim().split("\\s+".toRegex())
                            if (parts.size >= 2) "${parts.first().first().uppercaseChar()}${parts.last().first().uppercaseChar()}"
                            else username.first().uppercaseChar().toString()
                        } else "N"
                        Text(
                            text = initials,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextBlack
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = greeting(username),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextBlack,
                            letterSpacing = (-0.4).sp
                        )
                        Text(
                            text = "$reviewsToday reviews completed today",
                            fontSize = 13.sp,
                            color = TextTertiary
                        )
                    }
                }
            }

            // Due Queue Banner
            item {
                DueQueueBanner(
                    dueCount = dueCount,
                    onStartReview = { onNavigateToReview(null) }
                )
            }

            // Quick Stats
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Total Cards",
                        value = "${retention.totalCards}",
                        subtitle = "${retention.matureCards} mastered",
                        accentColor = TextBlack,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "In Progress",
                        value = "${retention.learningCards + retention.youngCards}",
                        subtitle = "${retention.learningCards} new, ${retention.youngCards} young",
                        accentColor = TextBlack,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Subject Decks Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Subject Decks",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextBlack
                    )
                    Text(
                        text = "${decks.size} decks",
                        fontSize = 13.sp,
                        color = TextTertiary
                    )
                }
            }

            // Deck List
            if (decks.isEmpty()) {
                item { EmptyDeckState(onIngestClick = onNavigateToIngestion) }
            } else {
                items(decks, key = { it.id }) { deck ->
                    DeckItemCard(
                        deck = deck,
                        onDeckClick = { onNavigateToDeckDetail(deck.id) },
                        onStudyClick = { onNavigateToReview(deck.id) },
                        onDeleteClick = { viewModel.deleteDeck(deck.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showCreateDeckDialog) {
        CreateDeckDialog(
            onDismiss = { showCreateDeckDialog = false },
            onCreate = { name, desc, category, color ->
                viewModel.createDeck(name, desc, category, color)
                showCreateDeckDialog = false
            }
        )
    }
}

private fun greeting(name: String): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val timeOfDay = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }
    return if (name.isNotBlank()) "$timeOfDay, $name" else timeOfDay
}

@Composable
private fun DueQueueBanner(
    dueCount: Int,
    onStartReview: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = if (dueCount > 0) TextBlack else LightGray
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = if (dueCount > 0) "$dueCount cards due" else "All caught up",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (dueCount > 0) White else TextBlack
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (dueCount > 0)
                    "Spaced repetition schedule calibrated for optimal memory retention."
                else
                    "No pending reviews. Ingest lecture notes or ask AI questions to build cards.",
                fontSize = 14.sp,
                color = if (dueCount > 0) White.copy(alpha = 0.75f) else TextTertiary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onStartReview,
                enabled = dueCount > 0,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (dueCount > 0) White else BorderLight,
                    contentColor = if (dueCount > 0) TextBlack else TextTertiary,
                    disabledContainerColor = BorderLight,
                    disabledContentColor = TextTertiary
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (dueCount > 0) "Start Review" else "No Reviews Due",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun DeckItemCard(
    deck: DeckWithStats,
    onDeckClick: () -> Unit,
    onStudyClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDeckClick() },
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = deck.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = LightGray
                ) {
                    Text(
                        text = deck.category.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            if (deck.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = deck.description,
                    fontSize = 13.sp,
                    color = TextTertiary,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            val progress = if (deck.totalCards > 0) deck.masteredCards.toFloat() / deck.totalCards else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = TextBlack,
                trackColor = LightGray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${deck.totalCards} cards · ${deck.dueCards} due",
                    fontSize = 13.sp,
                    color = if (deck.dueCards > 0) AccentBlue else TextTertiary,
                    fontWeight = FontWeight.Medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (deck.dueCards > 0) {
                        Button(
                            onClick = onStudyClick,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TextBlack,
                                contentColor = White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text("Study", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    TextButton(onClick = { showDeleteConfirm = true }) {
                        Text("Delete", fontSize = 12.sp, color = SystemRed.copy(alpha = 0.7f))
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Deck", color = TextBlack, fontWeight = FontWeight.Bold) },
            text = { Text("Delete '${deck.name}' and all its cards?", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteClick()
                    showDeleteConfirm = false
                }) {
                    Text("Delete", color = SystemRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel", color = TextBlack)
                }
            },
            containerColor = White
        )
    }
}

@Composable
private fun EmptyDeckState(onIngestClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LightGray
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No Decks Yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Ingest your lecture notes or formula sheets to automatically generate flashcards.",
                fontSize = 14.sp,
                color = TextTertiary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onIngestClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextBlack,
                    contentColor = White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Get Started", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CreateDeckDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, DeckCategory, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(DeckCategory.ENGINEERING) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Deck", color = TextBlack, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Deck Name") },
                    placeholder = { Text("e.g., Fluid Mechanics") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TextBlack,
                        unfocusedBorderColor = BorderLight,
                        focusedTextColor = TextBlack,
                        unfocusedTextColor = TextBlack
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TextBlack,
                        unfocusedBorderColor = BorderLight,
                        focusedTextColor = TextBlack,
                        unfocusedTextColor = TextBlack
                    )
                )

                Text("Category", fontSize = 13.sp, color = TextTertiary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DeckCategory.values().take(3).forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCategory = cat },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) TextBlack else LightGray
                        ) {
                            Text(
                                text = cat.name.replace("_", " "),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) White else TextSecondary,
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onCreate(name, description, selectedCategory, selectedCategory.hexColor)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextBlack,
                    contentColor = White
                )
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = White
    )
}

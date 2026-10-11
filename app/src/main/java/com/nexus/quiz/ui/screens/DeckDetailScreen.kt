package com.nexus.quiz.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.ui.components.MathView
import com.nexus.quiz.ui.components.NexusTopAppBar
import com.nexus.quiz.ui.theme.BorderLight
import com.nexus.quiz.ui.theme.LightGray
import com.nexus.quiz.ui.theme.SystemGreen
import com.nexus.quiz.ui.theme.SystemRed
import com.nexus.quiz.ui.theme.TextBlack
import com.nexus.quiz.ui.theme.TextPrimary
import com.nexus.quiz.ui.theme.TextSecondary
import com.nexus.quiz.ui.theme.TextTertiary
import com.nexus.quiz.ui.theme.White
import com.nexus.quiz.ui.viewmodel.DeckDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailScreen(
    viewModel: DeckDetailViewModel,
    onNavigateBack: () -> Unit,
    onStartReview: () -> Unit
) {
    val deck by viewModel.deck.collectAsState()
    val cards by viewModel.cards.collectAsState()

    var showAddCardDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NexusTopAppBar(
                title = deck?.name ?: "Deck",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    if (cards.isNotEmpty()) {
                        IconButton(onClick = onStartReview) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Study", tint = TextBlack)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCardDialog = true },
                containerColor = TextBlack,
                contentColor = White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Card")
            }
        },
        containerColor = White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Deck Header Info
            item {
                deck?.let { d ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = LightGray
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = White
                                ) {
                                    Text(
                                        text = d.category.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = "${cards.size} cards",
                                    fontSize = 13.sp,
                                    color = TextTertiary
                                )
                            }

                            if (d.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(d.description, fontSize = 14.sp, color = TextSecondary)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onStartReview,
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TextBlack, contentColor = White)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Study Deck", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Cards Header
            item {
                Text("Cards", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            }

            if (cards.isEmpty()) {
                item { Text("No cards yet. Add manually or ingest notes.", fontSize = 14.sp, color = TextTertiary) }
            } else {
                items(cards, key = { it.id }) { card ->
                    DeckCardListItem(card = card, onDelete = { viewModel.deleteCard(card.id) })
                }
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }

    if (showAddCardDialog) {
        AddManualCardDialog(
            onDismiss = { showAddCardDialog = false },
            onAdd = { type, prompt, answer, explanation, formula, cloze ->
                viewModel.addCard(type, prompt, answer, explanation, formula, cloze)
                showAddCardDialog = false
            }
        )
    }
}

@Composable
private fun DeckCardListItem(card: CardEntity, onDelete: () -> Unit) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LightGray
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = White
                ) {
                    Text(
                        text = card.type.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${card.interval}d · EF ${"%.1f".format(card.easeFactor)}",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(onClick = { showDeleteConfirm = true }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SystemRed.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MathView(text = card.prompt, latexFormula = card.latexFormula, isRevealed = true, minHeightDp = 30)

            Spacer(modifier = Modifier.height(4.dp))

            Text("Ans: ${card.answer}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = SystemGreen)
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Card", color = TextBlack) },
            text = { Text("Delete this card permanently?", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteConfirm = false }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddManualCardDialog(
    onDismiss: () -> Unit,
    onAdd: (CardType, String, String, String, String?, String?) -> Unit
) {
    var selectedType by remember { mutableStateOf(CardType.ATOMIC_FACT) }
    var prompt by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var explanation by remember { mutableStateOf("") }
    var formula by remember { mutableStateOf("") }
    var expandedTypeDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Card", color = TextBlack, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandedTypeDropdown,
                    onExpandedChange = { expandedTypeDropdown = !expandedTypeDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedType.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Card Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTypeDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TextBlack, unfocusedBorderColor = BorderLight, focusedTextColor = TextBlack, unfocusedTextColor = TextBlack)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTypeDropdown,
                        onDismissRequest = { expandedTypeDropdown = false },
                        modifier = Modifier.background(White)
                    ) {
                        CardType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName, color = TextBlack) },
                                onClick = { selectedType = type; expandedTypeDropdown = false }
                            )
                        }
                    }
                }

                OutlinedTextField(value = prompt, onValueChange = { prompt = it }, label = { Text("Question / Prompt") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TextBlack, unfocusedBorderColor = BorderLight, focusedTextColor = TextBlack, unfocusedTextColor = TextBlack))
                OutlinedTextField(value = answer, onValueChange = { answer = it }, label = { Text("Answer") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TextBlack, unfocusedBorderColor = BorderLight, focusedTextColor = TextBlack, unfocusedTextColor = TextBlack))
                OutlinedTextField(value = formula, onValueChange = { formula = it }, label = { Text("LaTeX Formula (optional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TextBlack, unfocusedBorderColor = BorderLight, focusedTextColor = TextBlack, unfocusedTextColor = TextBlack))
                OutlinedTextField(value = explanation, onValueChange = { explanation = it }, label = { Text("Explanation") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TextBlack, unfocusedBorderColor = BorderLight, focusedTextColor = TextBlack, unfocusedTextColor = TextBlack))
            }
        },
        confirmButton = {
            Button(
                onClick = { if (prompt.isNotBlank() && answer.isNotBlank()) onAdd(selectedType, prompt, answer, explanation, formula.ifBlank { null }, null) },
                enabled = prompt.isNotBlank() && answer.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = TextBlack, contentColor = White)
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) } },
        containerColor = White
    )
}

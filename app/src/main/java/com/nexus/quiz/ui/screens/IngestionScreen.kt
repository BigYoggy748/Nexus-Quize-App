package com.nexus.quiz.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.data.model.FundamentalRuleDto
import com.nexus.quiz.data.model.GradedCardDto
import com.nexus.quiz.data.model.RealWorldApplicationDto
import com.nexus.quiz.data.model.WorkedExampleDto
import com.nexus.quiz.ui.components.MathView
import com.nexus.quiz.ui.components.NexusTopAppBar
import com.nexus.quiz.ui.theme.AccentBlue
import com.nexus.quiz.ui.theme.AntiqueGold
import com.nexus.quiz.ui.theme.BorderLight
import com.nexus.quiz.ui.theme.BorderMedium
import com.nexus.quiz.ui.theme.DeepPine
import com.nexus.quiz.ui.theme.DiffFundamental
import com.nexus.quiz.ui.theme.DiffHard
import com.nexus.quiz.ui.theme.DiffMedium
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
import com.nexus.quiz.ui.theme.WarmBackground
import com.nexus.quiz.ui.theme.WarmParchment
import com.nexus.quiz.ui.theme.White
import com.nexus.quiz.ui.viewmodel.IngestionUiState
import com.nexus.quiz.ui.viewmodel.IngestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngestionScreen(
    viewModel: IngestionViewModel,
    onNavigateBack: () -> Unit,
    onSavedSuccessfully: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val decks by viewModel.decks.collectAsState()
    val savedTopics by viewModel.savedTopics.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val deepMastery by viewModel.deepMastery.collectAsState()
    val gradedCards by viewModel.gradedCards.collectAsState()
    val difficultyFilter by viewModel.difficultyFilter.collectAsState()

    var rawNotes by remember { mutableStateOf("") }
    var topicHint by remember { mutableStateOf("") }
    var selectedDeckId by remember { mutableStateOf<Long?>(null) }
    var newDeckName by remember { mutableStateOf("") }
    var isNewDeckMode by remember { mutableStateOf(false) }
    var expandedDropdown by remember { mutableStateOf(false) }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var learnSubTab by remember { mutableIntStateOf(0) } // 0: Deconstruct, 1: Saved Topics

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setSelectedImage(context, uri)
    }

    Scaffold(
        topBar = {
            NexusTopAppBar(
                title = if (deepMastery != null) "Concept Breakdown" else "Learn & Deconstruct",
                showBackButton = true,
                onBackClick = {
                    if (deepMastery != null) {
                        viewModel.resetState()
                    } else {
                        onNavigateBack()
                    }
                }
            )
        },
        containerColor = WarmBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mode 1: Input & Ask Questions Form or Saved Explanations Library
            if (deepMastery == null && uiState !is IngestionUiState.DeepMasterySuccess) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Learn Any Concept",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPine,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ask a question, enter a tricky topic (like Logarithms or Fluid Mechanics), or upload lecture slides. AI breaks it down into plain-English intuition before creating active recall quizzes.",
                        fontSize = 15.sp,
                        color = TextTertiary,
                        lineHeight = 22.sp
                    )
                }

                item {
                    // Segmented pill control: "Deconstruct New" vs "Saved Explanations"
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = LightGray,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                    ) {
                        Row(modifier = Modifier.padding(4.dp)) {
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { learnSubTab = 0 },
                                color = if (learnSubTab == 0) White else Color.Transparent,
                                shadowElevation = if (learnSubTab == 0) 1.dp else 0.dp
                            ) {
                                Text(
                                    text = "✨ Deconstruct New",
                                    fontSize = 13.sp,
                                    fontWeight = if (learnSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                                    color = if (learnSubTab == 0) DeepPine else TextTertiary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { learnSubTab = 1 },
                                color = if (learnSubTab == 1) White else Color.Transparent,
                                shadowElevation = if (learnSubTab == 1) 1.dp else 0.dp
                            ) {
                                Text(
                                    text = "📚 Saved Topics (${savedTopics.size})",
                                    fontSize = 13.sp,
                                    fontWeight = if (learnSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                                    color = if (learnSubTab == 1) DeepPine else TextTertiary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                if (learnSubTab == 0) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            // 1. Target Topic / Question Field
                            OutlinedTextField(
                                value = topicHint,
                                onValueChange = { topicHint = it },
                                label = { Text("What topic or question do you want to master?") },
                                placeholder = { Text("e.g. Logarithms, Bernoulli Equation, GST concepts...") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForestGreen,
                                    unfocusedBorderColor = BorderLight,
                                    focusedTextColor = TextBlack,
                                    unfocusedTextColor = TextBlack,
                                    focusedContainerColor = White,
                                    unfocusedContainerColor = White
                                )
                            )

                            // 2. Raw Notes / Problem Details
                            OutlinedTextField(
                                value = rawNotes,
                                onValueChange = { rawNotes = it },
                                label = { Text("Paste Notes, Formulas, or Specific Questions (Optional)") },
                                placeholder = { Text("Paste textbook excerpt, lecture notes, or specific problem you're stuck on...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForestGreen,
                                    unfocusedBorderColor = BorderLight,
                                    focusedTextColor = TextBlack,
                                    unfocusedTextColor = TextBlack,
                                    focusedContainerColor = White,
                                    unfocusedContainerColor = White
                                )
                            )

                            // 3. Upload Slide / Screenshot Zone
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        if (selectedImageUri == null) imagePickerLauncher.launch("image/*")
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                            ) {
                                Box(modifier = Modifier.padding(16.dp)) {
                                    if (selectedImageUri != null) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text("Slide / Photo Attached", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DeepPine)
                                                    Text("Multimodal AI will read formulas & diagrams", fontSize = 12.sp, color = TextTertiary)
                                                }
                                            }

                                            IconButton(
                                                onClick = { viewModel.clearSelectedImage() },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = SystemRed, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    } else {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AddPhotoAlternate,
                                                contentDescription = "Upload",
                                                tint = ForestGreen,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(14.dp))
                                            Column {
                                                Text(
                                                    text = "Attach Slide or Photo (Optional)",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = DeepPine
                                                )
                                                Text(
                                                    text = "Upload textbook screenshots or lecture presentation slides",
                                                    fontSize = 12.sp,
                                                    color = TextTertiary
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Primary Action Button
                            Button(
                                onClick = { viewModel.analyzeAndBreakdown(rawNotes, topicHint) },
                                enabled = (rawNotes.isNotBlank() || selectedImageUri != null || topicHint.isNotBlank()) && uiState !is IngestionUiState.Loading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DeepPine,
                                    contentColor = White,
                                    disabledContainerColor = BorderLight,
                                    disabledContentColor = TextTertiary
                                )
                            ) {
                                if (uiState is IngestionUiState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Analyzing & Deconstructing...", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Deconstruct & Teach Me", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                } else {
                    // Saved Explanations Tab
                    if (savedTopics.isEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                            ) {
                                Column(
                                    modifier = Modifier.padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "No Saved Explanations Yet",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepPine
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "When you deconstruct a topic (like Logarithms), its complete explanations, worked examples, and generated cards will be preserved here so you can revisit them anytime.",
                                        fontSize = 13.sp,
                                        color = TextTertiary,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 18.sp
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { learnSubTab = 0 },
                                        colors = ButtonDefaults.buttonColors(containerColor = DeepPine, contentColor = White),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Deconstruct a Topic", fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                    } else {
                        itemsIndexed(savedTopics) { _, saved ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                                shadowElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = SageGreen.copy(alpha = 0.15f)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Lightbulb,
                                                    contentDescription = null,
                                                    tint = ForestGreen,
                                                    modifier = Modifier.padding(8.dp).size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = saved.topicTitle,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepPine
                                                )
                                                Text(
                                                    text = "${saved.cardCount} active recall cards generated",
                                                    fontSize = 12.sp,
                                                    color = TextTertiary
                                                )
                                            }
                                        }

                                        IconButton(onClick = { viewModel.deleteSavedTopic(saved.id) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = TextTertiary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { viewModel.loadSavedTopic(saved) },
                                        modifier = Modifier.fillMaxWidth().height(42.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = DeepPine,
                                            contentColor = White
                                        )
                                    ) {
                                        Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Revisit Explanations & Cards", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Error Display
            if (uiState is IngestionUiState.Error) {
                item {
                    val errorMsg = (uiState as IngestionUiState.Error).message
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = SystemRed.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = SystemRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = errorMsg, color = SystemRed, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Mode 2: Deep Concept Mastery Hub (Generated breakdown)
            if (deepMastery != null) {
                val mastery = deepMastery!!

                // Topic Title Banner
                item {
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
                                        text = "CONCEPT BREAKDOWN",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                TextButton(onClick = { viewModel.resetState() }) {
                                    Text("New Search", color = TextBlack, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = mastery.topicTitle,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack
                            )
                        }
                    }
                }

                // Apple-style segmented tabs
                item {
                    val tabs = listOf("💡 Intuition", "📐 Examples", "📄 Slides", "🧠 Cards (${gradedCards.size})")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightGray)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val isSelected = selectedTabIndex == index
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedTabIndex = index },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) White else Color.Transparent,
                                shadowElevation = if (isSelected) 1.dp else 0.dp
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) TextBlack else TextTertiary,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // TAB 0: Core Intuition & Everyday Applications
                if (selectedTabIndex == 0) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = White,
                            shadowElevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = TextBlack, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("What is it actually? (Core Intuition)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                MathView(
                                    text = mastery.coreIntuition,
                                    isRevealed = true,
                                    minHeightDp = 50
                                )
                            }
                        }
                    }

                    if (mastery.whyItExists.isNotBlank()) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = LightGray
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text("Why was it invented?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = mastery.whyItExists,
                                        fontSize = 14.sp,
                                        color = TextSecondary,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Everyday Applications (${mastery.everydayApplications.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextBlack
                        )
                    }

                    itemsIndexed(mastery.everydayApplications) { _, app ->
                        ApplicationItem(app)
                    }

                    if (mastery.fundamentalRules.isNotEmpty()) {
                        item {
                            Text(
                                text = "Fundamental Rules (${mastery.fundamentalRules.size})",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack
                            )
                        }

                        itemsIndexed(mastery.fundamentalRules) { _, rule ->
                            RuleItem(rule)
                        }
                    }
                }

                // TAB 1: Worked Step-by-Step Examples
                if (selectedTabIndex == 1) {
                    if (mastery.workedExamples.isEmpty()) {
                        item { Text("No worked examples generated for this topic.", color = TextTertiary) }
                    } else {
                        itemsIndexed(mastery.workedExamples) { index, example ->
                            WorkedExampleItem(index + 1, example)
                        }
                    }
                }

                // TAB 2: Slide Analysis
                if (selectedTabIndex == 2) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = White,
                            shadowElevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = TextBlack, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Slide & Material Breakdown", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                if (!mastery.slideSpecificAnalysis.isNullOrBlank()) {
                                    MathView(
                                        text = mastery.slideSpecificAnalysis,
                                        isRevealed = true,
                                        minHeightDp = 60
                                    )
                                } else {
                                    Text(
                                        text = "General conceptual breakdown generated from topic input.",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // TAB 3: Graded Active Recall Deck
                if (selectedTabIndex == 3) {
                    // Difficulty Filters
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("ALL", "FUNDAMENTAL", "MEDIUM", "HARD").forEach { diff ->
                                val isSelected = difficultyFilter == diff
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.setDifficultyFilter(diff) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) TextBlack else LightGray
                                ) {
                                    Text(
                                        text = diff,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) White else TextSecondary,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Save Destination Selection Card
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = LightGray
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text("Save Cards to Spaced Repetition", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { isNewDeckMode = false },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (!isNewDeckMode) TextBlack else White
                                    ) {
                                        Text(
                                            text = "Existing Deck",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (!isNewDeckMode) White else TextSecondary,
                                            modifier = Modifier.padding(10.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { isNewDeckMode = true },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isNewDeckMode) TextBlack else White
                                    ) {
                                        Text(
                                            text = "New Deck",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isNewDeckMode) White else TextSecondary,
                                            modifier = Modifier.padding(10.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                if (isNewDeckMode) {
                                    OutlinedTextField(
                                        value = newDeckName,
                                        onValueChange = { newDeckName = it },
                                        label = { Text("New Deck Name") },
                                        placeholder = { Text(mastery.topicTitle) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = TextBlack,
                                            unfocusedBorderColor = BorderLight,
                                            focusedTextColor = TextBlack,
                                            unfocusedTextColor = TextBlack,
                                            focusedContainerColor = White,
                                            unfocusedContainerColor = White
                                        )
                                    )
                                } else {
                                    if (decks.isNotEmpty()) {
                                        ExposedDropdownMenuBox(
                                            expanded = expandedDropdown,
                                            onExpandedChange = { expandedDropdown = !expandedDropdown }
                                        ) {
                                            val currentDeckName = decks.find { it.id == selectedDeckId }?.name
                                                ?: decks.firstOrNull()?.name ?: "Select Deck"

                                            OutlinedTextField(
                                                value = currentDeckName,
                                                onValueChange = {},
                                                readOnly = true,
                                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                                shape = RoundedCornerShape(10.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = TextBlack,
                                                    unfocusedBorderColor = BorderLight,
                                                    focusedTextColor = TextBlack,
                                                    unfocusedTextColor = TextBlack,
                                                    focusedContainerColor = White,
                                                    unfocusedContainerColor = White
                                                )
                                            )

                                            ExposedDropdownMenu(
                                                expanded = expandedDropdown,
                                                onDismissRequest = { expandedDropdown = false },
                                                modifier = Modifier.background(White)
                                            ) {
                                                decks.forEach { d ->
                                                    DropdownMenuItem(
                                                        text = { Text(d.name, color = TextBlack) },
                                                        onClick = {
                                                            selectedDeckId = d.id
                                                            expandedDropdown = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        val targetId = if (isNewDeckMode) -1L else (selectedDeckId ?: decks.firstOrNull()?.id ?: -1L)
                                        viewModel.saveGradedCardsToDeck(targetId, newDeckName.ifBlank { mastery.topicTitle }, difficultyFilter)
                                    },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TextBlack, contentColor = White)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    val count = if (difficultyFilter == "ALL") gradedCards.size else gradedCards.count { it.difficulty.equals(difficultyFilter, true) }
                                    Text("Add $count Cards to Spaced Repetition", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    // Graded Cards List
                    val filteredCards = if (difficultyFilter == "ALL") gradedCards else gradedCards.filter { it.difficulty.equals(difficultyFilter, true) }
                    itemsIndexed(filteredCards) { index, card ->
                        GradedCardItem(
                            index = index + 1,
                            card = card,
                            onDelete = { viewModel.removeGradedCard(index) }
                        )
                    }
                }
            }

            // Saved Confirmation Box
            if (uiState is IngestionUiState.Saved) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = SystemGreen.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SystemGreen, modifier = Modifier.size(44.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Cards Added Successfully", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Your cards have been placed in the SM-2 spaced repetition queue.", fontSize = 14.sp, color = TextSecondary, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = onSavedSuccessfully,
                                colors = ButtonDefaults.buttonColors(containerColor = TextBlack, contentColor = White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Go to Review", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun ApplicationItem(app: RealWorldApplicationDto) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LightGray
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        text = app.field.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Icon(Icons.Default.Public, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(app.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Spacer(modifier = Modifier.height(6.dp))
            Text(app.explanation, fontSize = 14.sp, color = TextSecondary, lineHeight = 20.sp)

            if (!app.latexFormula.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                MathView(text = "", latexFormula = app.latexFormula, isRevealed = true, minHeightDp = 30)
            }
        }
    }
}

@Composable
private fun RuleItem(rule: FundamentalRuleDto) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(rule.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Spacer(modifier = Modifier.height(6.dp))
            MathView(text = "", latexFormula = rule.latexFormula, isRevealed = true, minHeightDp = 32)
            if (rule.intuition.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Intuition: ${rule.intuition}", fontSize = 13.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun WorkedExampleItem(index: Int, example: WorkedExampleDto) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Example $index: ${example.problemTitle}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = LightGray
                ) {
                    Text(
                        text = example.difficulty.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MathView(text = "Problem: ${example.problemStatement}", isRevealed = true, minHeightDp = 30)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Step-by-Step Derivation:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextTertiary)
            Spacer(modifier = Modifier.height(4.dp))

            example.steps.forEach { step ->
                MathView(text = step, isRevealed = true, minHeightDp = 24)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = SystemGreen.copy(alpha = 0.08f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Answer: ${example.finalAnswer}", fontWeight = FontWeight.Bold, color = SystemGreen, fontSize = 14.sp)
                    if (example.keyTakeaway.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Takeaway: ${example.keyTakeaway}", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun GradedCardItem(
    index: Int,
    card: GradedCardDto,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = LightGray
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        text = "${card.difficulty} · ${card.type.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (card.difficulty.uppercase()) {
                            "HARD" -> DiffHard
                            "FUNDAMENTAL" -> DiffFundamental
                            else -> DiffMedium
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = SystemRed.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MathView(text = card.prompt, latexFormula = card.latexFormula, isRevealed = true, minHeightDp = 30)

            Spacer(modifier = Modifier.height(6.dp))

            Text("Ans: ${card.answer}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = SystemGreen)
        }
    }
}

package com.nexus.quiz.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.data.local.PreferencesManager
import com.nexus.quiz.ui.components.NexusTopAppBar
import com.nexus.quiz.ui.theme.AccentBlue
import com.nexus.quiz.ui.theme.BorderLight
import com.nexus.quiz.ui.theme.LightGray
import com.nexus.quiz.ui.theme.SystemGreen
import com.nexus.quiz.ui.theme.TextBlack
import com.nexus.quiz.ui.theme.TextPrimary
import com.nexus.quiz.ui.theme.TextSecondary
import com.nexus.quiz.ui.theme.TextTertiary
import com.nexus.quiz.ui.theme.White
import com.nexus.quiz.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val currentApiKey by viewModel.apiKey.collectAsState()
    val currentModel by viewModel.selectedModel.collectAsState()
    val currentUsername by viewModel.username.collectAsState()
    val saveStatus by viewModel.saveStatus.collectAsState()

    var usernameInput by remember(currentUsername) { mutableStateOf(currentUsername) }
    var apiKeyInput by remember(currentApiKey) { mutableStateOf(currentApiKey) }
    var showPassword by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NexusTopAppBar(title = "Settings")
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
            // Save confirmation banner
            item {
                AnimatedVisibility(visible = saveStatus != null) {
                    saveStatus?.let { status ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = SystemGreen.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SystemGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(status, color = SystemGreen, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            // 1. Profile / Account Section
            item {
                Text(
                    text = "PROFILE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LightGray
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Avatar circle
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = viewModel.getAvatarInitials(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextBlack
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (currentUsername.isNotBlank()) currentUsername else "Guest Learner",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextBlack
                                )
                                Text(
                                    text = "Nexus Cognitive Memory Engine",
                                    fontSize = 12.sp,
                                    color = TextTertiary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = usernameInput,
                            onValueChange = { usernameInput = it },
                            label = { Text("Display Name") },
                            placeholder = { Text("e.g. Alex, David") },
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.updateUsername(usernameInput) },
                            enabled = usernameInput.isNotBlank() && usernameInput != currentUsername,
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TextBlack,
                                contentColor = White,
                                disabledContainerColor = BorderLight,
                                disabledContentColor = TextTertiary
                            )
                        ) {
                            Text("Save Name", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // 2. Gemini AI Intelligence Section
            item {
                Text(
                    text = "ARTIFICIAL INTELLIGENCE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LightGray
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("Gemini API Key", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your API key is stored securely and exclusively on your device to power multimodal topic deconstruction and active recall question generation.",
                            fontSize = 13.sp,
                            color = TextTertiary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = apiKeyInput,
                            onValueChange = { apiKeyInput = it },
                            label = { Text("API Key") },
                            placeholder = { Text("AIzaSy...") },
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle key visibility",
                                        tint = TextTertiary
                                    )
                                }
                            },
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.updateApiKey(apiKeyInput) },
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TextBlack, contentColor = White)
                        ) {
                            Text("Save API Key", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // 3. Model Architecture Selection
            item {
                Text(
                    text = "AI MODEL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LightGray
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        PreferencesManager.AVAILABLE_MODELS.forEach { (modelId, description) ->
                            val isSelected = currentModel == modelId
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.updateModel(modelId) }
                                    .padding(vertical = 10.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.updateModel(modelId) },
                                    colors = RadioButtonDefaults.colors(selectedColor = TextBlack, unselectedColor = BorderLight)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = description,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = TextBlack
                                    )
                                    Text(
                                        text = modelId,
                                        fontSize = 12.sp,
                                        color = TextTertiary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Privacy & Principles
            item {
                Text(
                    text = "PRIVACY & ARCHITECTURE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LightGray
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("• Local-First Privacy: All your study cards and review logs stay 100% on your device.", fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Mathematical Spaced Repetition: SuperMemo SM-2 algorithm manages your exact optimal review dates.", fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• First-Principles Reasoning: AI teaches intuition before testing retention.", fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

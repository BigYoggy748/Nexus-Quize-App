package com.nexus.quiz.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.ui.theme.AccentBlue
import com.nexus.quiz.ui.theme.BorderLight
import com.nexus.quiz.ui.theme.TextTertiary
import com.nexus.quiz.ui.theme.White

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : BottomNavItem("home", "Decks", Icons.Outlined.Layers, Icons.Filled.Layers)
    object Ingestion : BottomNavItem("ingestion", "Learn", Icons.Outlined.AutoAwesome, Icons.Outlined.AutoAwesome)
    object Analytics : BottomNavItem("analytics", "Analytics", Icons.Outlined.BarChart, Icons.Filled.BarChart)
    object Settings : BottomNavItem("settings", "Settings", Icons.Outlined.Settings, Icons.Filled.Settings)
}

@Composable
fun NexusBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Ingestion,
        BottomNavItem.Analytics,
        BottomNavItem.Settings
    )

    NavigationBar(
        containerColor = White,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        item.title,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                selected = selected,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentBlue,
                    selectedTextColor = AccentBlue,
                    indicatorColor = AccentBlue.copy(alpha = 0.1f),
                    unselectedIconColor = TextTertiary,
                    unselectedTextColor = TextTertiary
                )
            )
        }
    }
}

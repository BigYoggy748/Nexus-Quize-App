package com.nexus.quiz.data.model

enum class DeckCategory(val title: String, val iconName: String, val hexColor: String) {
    ENGINEERING(
        title = "Engineering Practices",
        iconName = "Cpu",
        hexColor = "#38BDF8" // Cyan
    ),
    MATHEMATICS(
        title = "Math & Formulas",
        iconName = "Calculator",
        hexColor = "#818CF8" // Indigo
    ),
    GST_STUDIES(
        title = "GST & General Studies",
        iconName = "BookOpen",
        hexColor = "#F59E0B" // Amber
    ),
    PHYSICS_CHEMISTRY(
        title = "Physical Sciences",
        iconName = "Atom",
        hexColor = "#34D399" // Emerald
    ),
    CUSTOM(
        title = "General & Custom",
        iconName = "Layers",
        hexColor = "#A855F7" // Purple
    )
}

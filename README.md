# Nexus — Cognitive Memory Engine (Android / Kotlin)

**Nexus** is a native Android application written in **Kotlin** using **Jetpack Compose** and **Room SQLite**, engineered specifically for high-retention technical memorization (mathematics formulas, engineering principles, physical sciences, and GST / college subjects).

---

## Key Cognitive Features

### 1. AI-Assisted Atomic Note Shredding (Google Gemini)
- Paste raw lecture notes, formula sheets, or syllabus chapters.
- Uses Gemini API with **Structured JSON Schema Output** (`response_mime_type="application/json"`).
- Enforces the **Rule of Atomicity**: exactly one discrete mental leap per card to eliminate cognitive overload.
- Review staging area: inspect, edit, or delete generated cards before committing to the local Room database.

### 2. Multi-Mode Cognitive Testing Suite
- **Cloze Math Occlusions**: Occludes variables in LaTeX equations ($PV = nR\underline{\quad}$) with click/tap-to-reveal.
- **Feynman AI Auditing**: Type a concise 2-sentence explanation of a physical mechanism. Gemini audits your response against required key terms, flags misconceptions/hallucinations, assigns a score (0-100), and provides constructive critique.
- **Step-by-Step Derivations**: Sequential proof walkthroughs ($n \to n+1$).
- **Standard Atomic Recall**: Prompt $\to$ Active Recall $\to$ Answer Reveal $\to$ Honest Self-Assessment.

### 3. Spaced Repetition (SM-2) Scheduling Engine
- Native implementation of the SM-2 algorithm:
  - Tracks `interval` (days), `repetitions`, `easeFactor` (default 2.5), `lapses`, and `dueDate`.
  - Honest self-grading buttons: `Again (1)`, `Hard (2)`, `Good (3)`, `Easy (4)` with dynamic interval previews.
  - Automatically schedules failed cards for same-day relearning and matures cards over 21+ day intervals.

### 4. Mathematical Typography (KaTeX)
- Inline (`$...$`) and block (`$$...$$`) LaTeX typography rendered with KaTeX.
- Works **100% offline** for study sessions.

### 5. Local-First Architecture
- All decks, cards, and review logs are stored locally on your device in **Room SQLite**.
- Gemini API keys and preferences are stored locally in private `SharedPreferences`.

---

## Project Architecture

```
app/src/main/
├── assets/
│   └── katex/math_template.html     # Bundled KaTeX template for math & cloze
├── java/com/nexus/quiz/
│   ├── MainActivity.kt              # Main Compose Activity
│   ├── NexusApplication.kt         # Application class & DI wiring
│   ├── data/
│   │   ├── model/                  # CardType, SrsRating, DeckCategory, Gemini DTOs
│   │   ├── local/
│   │   │   ├── NexusDatabase.kt     # Room Database with starter decks
│   │   │   ├── PreferencesManager.kt# Gemini API key & model settings
│   │   │   ├── dao/                 # DeckDao, CardDao, ReviewLogDao
│   │   │   └── entity/              # DeckEntity, CardEntity, ReviewLogEntity
│   │   └── repository/              # NexusRepository
│   ├── engine/
│   │   ├── SrsScheduler.kt          # SM-2 algorithmic scheduler
│   │   ├── GeminiService.kt         # Structured JSON ingestion & Feynman grader
│   │   └── LatexParser.kt           # LaTeX & Cloze parser
│   └── ui/
│       ├── theme/                   # High-tech dark engineering theme
│       ├── components/              # MathView, RatingBar, StatCard, Top/Bottom Bars
│       ├── screens/                 # Home, Ingestion, ReviewSession, DeckDetail, Analytics, Settings
│       ├── navigation/              # Screen routes & NavGraph
│       └── viewmodel/               # Home, Ingestion, Review, DeckDetail, Analytics, Settings VMs
└── res/                             # Values, Drawables, Mipmaps, Backup XMLs
```

---

## How to Run in Android Studio

1. Open **Android Studio** (Koala / Ladybug / Meerkat or newer).
2. Click **File -> Open...** and select this directory: `C:\Users\hp\Desktop\Nexus Quiz App`.
3. Android Studio will automatically sync the Gradle project using Gradle 9.3 and Java 25.
4. Select your connected Android device or Android Emulator (API 26+).
5. Click **Run** (or `Shift + F10`) to launch Nexus!
6. Navigate to **Settings** tab in the app to enter your Google Gemini API key.

package com.nexus.quiz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nexus.quiz.data.local.dao.CardDao
import com.nexus.quiz.data.local.dao.DeckDao
import com.nexus.quiz.data.local.dao.ReviewLogDao
import com.nexus.quiz.data.local.dao.TopicMasteryDao
import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.local.entity.DeckEntity
import com.nexus.quiz.data.local.entity.ReviewLogEntity
import com.nexus.quiz.data.local.entity.TopicMasteryEntity
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.data.model.DeckCategory
import com.nexus.quiz.data.model.SrsRating
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromCardType(value: CardType): String = value.name

    @TypeConverter
    fun toCardType(value: String): CardType = try {
        CardType.valueOf(value)
    } catch (e: Exception) {
        CardType.ATOMIC_FACT
    }

    @TypeConverter
    fun fromSrsRating(value: SrsRating): String = value.name

    @TypeConverter
    fun toSrsRating(value: String): SrsRating = try {
        SrsRating.valueOf(value)
    } catch (e: Exception) {
        SrsRating.GOOD
    }

    @TypeConverter
    fun fromDeckCategory(value: DeckCategory): String = value.name

    @TypeConverter
    fun toDeckCategory(value: String): DeckCategory = try {
        DeckCategory.valueOf(value)
    } catch (e: Exception) {
        DeckCategory.ENGINEERING
    }
}

@Database(
    entities = [
        DeckEntity::class,
        CardEntity::class,
        ReviewLogEntity::class,
        TopicMasteryEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NexusDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract fun reviewLogDao(): ReviewLogDao
    abstract fun topicMasteryDao(): TopicMasteryDao

    companion object {
        @Volatile
        private var INSTANCE: NexusDatabase? = null

        fun getDatabase(context: Context): NexusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NexusDatabase::class.java,
                    "nexus_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabasePrepopulateCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabasePrepopulateCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedStarterDecks(database)
                    }
                }
            }
        }

        private suspend fun seedStarterDecks(database: NexusDatabase) {
            val now = System.currentTimeMillis()
            
            // 1. Engineering Deck
            val engDeckId = database.deckDao().insertDeck(
                DeckEntity(
                    name = "Fluid Mechanics & Hydraulics",
                    description = "Key principles, Bernoulli derivations, cavitation, Reynolds number",
                    category = DeckCategory.ENGINEERING,
                    hexColor = "#38BDF8",
                    createdAt = now,
                    updatedAt = now
                )
            )

            // 2. Math & Formulas Deck
            val mathDeckId = database.deckDao().insertDeck(
                DeckEntity(
                    name = "Calculus & Differential Equations",
                    description = "Integration techniques, Fourier transforms, Laplace transforms",
                    category = DeckCategory.MATHEMATICS,
                    hexColor = "#818CF8",
                    createdAt = now,
                    updatedAt = now
                )
            )

            // 3. GST & General Studies
            val gstDeckId = database.deckDao().insertDeck(
                DeckEntity(
                    name = "GST: Science, Tech & Society",
                    description = "Scientific revolutions, ethical engineering, Nigerian constitutional milestones",
                    category = DeckCategory.GST_STUDIES,
                    hexColor = "#F59E0B",
                    createdAt = now,
                    updatedAt = now
                )
            )

            // Seed sample high-retention atomic cards
            // Engineering Card 1: Cloze Bernoulli
            database.cardDao().insertCard(
                CardEntity(
                    deckId = engDeckId,
                    type = CardType.CLOZE_FORMULA,
                    prompt = "Bernoulli equation for incompressible, frictionless streamline flow balances pressure head, velocity head, and elevation head:",
                    answer = "\\frac{1}{2}\\rho v^2",
                    explanation = "Dynamic pressure term representing kinetic energy per unit volume.",
                    latexFormula = "P + [cloze]\\frac{1}{2}\\rho v^2[/cloze] + \\rho g z = \\text{constant}",
                    clozeOcclusion = "\\frac{1}{2}\\rho v^2",
                    dueDate = now
                )
            )

            // Engineering Card 2: Feynman Concept on Cavitation
            database.cardDao().insertCard(
                CardEntity(
                    deckId = engDeckId,
                    type = CardType.FEYNMAN_CONCEPT,
                    prompt = "Explain why cavitation occurs in hydraulic pump impellers and its physical consequence.",
                    answer = "Cavitation occurs when local static fluid pressure drops below the fluid's vapor pressure, forming vapor bubbles. When these bubbles flow into high-pressure regions, they violently collapse (implode), creating micro-jets and shockwaves that erode impeller metal surfaces.",
                    explanation = "Key factors: static pressure vs vapor pressure, bubble collapse/implosion, surface pitting.",
                    keyTermsJson = "[\"vapor pressure\", \"static pressure\", \"implosion\", \"bubble collapse\", \"pitting erosion\"]",
                    misconceptionsJson = "[\"high fluid viscosity\", \"boiling due to external heating\"]",
                    dueDate = now
                )
            )

            // Math Card 1: Cloze Integration by Parts
            database.cardDao().insertCard(
                CardEntity(
                    deckId = mathDeckId,
                    type = CardType.CLOZE_FORMULA,
                    prompt = "The standard Integration by Parts formula:",
                    answer = "v \\cdot du",
                    explanation = "Derived from the product rule of differentiation: d(uv) = u dv + v du.",
                    latexFormula = "\\int u \\, dv = u v - \\int [cloze]v \\, du[/cloze]",
                    clozeOcclusion = "v \\, du",
                    dueDate = now
                )
            )

            // Math Card 2: Derivation Step Card
            database.cardDao().insertCard(
                CardEntity(
                    deckId = mathDeckId,
                    type = CardType.DERIVATION_STEP,
                    prompt = "Deriving Euler's Formula: Expand \$e^{ix}\$ using Maclaurin series. What is step 2 grouping real and imaginary terms?",
                    answer = "\\left(1 - \\frac{x^2}{2!} + \\frac{x^4}{4!} - \\dots\\right) + i\\left(x - \\frac{x^3}{3!} + \\frac{x^5}{5!} - \\dots\\right) = \\cos(x) + i\\sin(x)",
                    explanation = "Separating the series into even powers of x (real cosine series) and odd powers multiplied by i (imaginary sine series).",
                    latexFormula = "e^{ix} = \\sum_{n=0}^{\\infty} \\frac{(ix)^n}{n!} = \\cos(x) + i\\sin(x)",
                    dueDate = now
                )
            )

            // GST Card 1: Atomic Fact
            database.cardDao().insertCard(
                CardEntity(
                    deckId = gstDeckId,
                    type = CardType.ATOMIC_FACT,
                    prompt = "What was the significance of the 1914 Amalgamation in Nigerian history?",
                    answer = "Lord Frederick Lugard unified the Northern Nigeria Protectorate and the Southern Nigeria Protectorate into a single colonial administrative entity called Nigeria.",
                    explanation = "Essential GST history milestone establishing Nigeria's modern territorial boundaries.",
                    dueDate = now
                )
            )
        }
    }
}

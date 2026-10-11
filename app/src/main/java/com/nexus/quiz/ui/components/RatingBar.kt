package com.nexus.quiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.model.SrsRating
import com.nexus.quiz.engine.SrsScheduler
import com.nexus.quiz.ui.theme.BorderLight
import com.nexus.quiz.ui.theme.LightGray
import com.nexus.quiz.ui.theme.RatingAgain
import com.nexus.quiz.ui.theme.RatingEasy
import com.nexus.quiz.ui.theme.RatingGood
import com.nexus.quiz.ui.theme.RatingHard
import com.nexus.quiz.ui.theme.TextBlack
import com.nexus.quiz.ui.theme.TextTertiary
import com.nexus.quiz.ui.theme.White

@Composable
fun RatingBar(
    card: CardEntity,
    onRatingSelected: (SrsRating) -> Unit,
    modifier: Modifier = Modifier,
    suggestedRating: SrsRating? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = LightGray,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HOW WELL DID YOU RECALL THIS?",
                fontSize = 11.sp,
                color = TextTertiary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RatingPill(
                    modifier = Modifier.weight(1f),
                    label = "Repeat",
                    intervalText = SrsScheduler.previewIntervalString(card, SrsRating.AGAIN),
                    color = RatingAgain,
                    isSuggested = suggestedRating == SrsRating.AGAIN,
                    onClick = { onRatingSelected(SrsRating.AGAIN) }
                )
                RatingPill(
                    modifier = Modifier.weight(1f),
                    label = "Hard",
                    intervalText = SrsScheduler.previewIntervalString(card, SrsRating.HARD),
                    color = RatingHard,
                    isSuggested = suggestedRating == SrsRating.HARD,
                    onClick = { onRatingSelected(SrsRating.HARD) }
                )
                RatingPill(
                    modifier = Modifier.weight(1f),
                    label = "Good",
                    intervalText = SrsScheduler.previewIntervalString(card, SrsRating.GOOD),
                    color = RatingGood,
                    isSuggested = suggestedRating == SrsRating.GOOD,
                    onClick = { onRatingSelected(SrsRating.GOOD) }
                )
                RatingPill(
                    modifier = Modifier.weight(1f),
                    label = "Easy",
                    intervalText = SrsScheduler.previewIntervalString(card, SrsRating.EASY),
                    color = RatingEasy,
                    isSuggested = suggestedRating == SrsRating.EASY,
                    onClick = { onRatingSelected(SrsRating.EASY) }
                )
            }
        }
    }
}

@Composable
private fun RatingPill(
    label: String,
    intervalText: String,
    color: Color,
    isSuggested: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 2.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSuggested) color else color.copy(alpha = 0.14f),
            contentColor = if (isSuggested) White else color
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                softWrap = false
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = intervalText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

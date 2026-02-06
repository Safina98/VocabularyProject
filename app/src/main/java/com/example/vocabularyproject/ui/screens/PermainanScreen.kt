package com.example.vocabularyproject.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocabularyproject.ui.widgetstyles.HomeScreenButtonStyles
import com.example.vocabularyproject.util.buttonGradientClolor
import com.example.vocabularyproject.util.cardGradientColors

@Composable
fun PermainanScreen() {
    // 1. The Box acts as the full-screen container
    var cardIndex by remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.2f)
    ) {
        AnimatedContent(
            targetState = cardIndex,
            transitionSpec = {
                // The new card slides up and fades in
                (slideInHorizontally { width->width } + fadeIn(animationSpec = tween(400)))
                    .togetherWith(
                        // The old card fades out and scales down slightly
                        fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f)
                    )
            },
            label = "CardSwitch"
        ) { targetIndex ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(16.dp)
                    .clickable { cardIndex++ },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize() // Use fillMaxSize to cover the internal area of the card
                        .background(brush = Brush.horizontalGradient(colors = cardGradientColors))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.clickable { },
                        text = "New",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Word") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    HomeScreenButtonStyles(
                        "Submit",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = buttonGradientClolor
                    )

                    Text(
                        modifier = Modifier.clickable { },
                        text = "Baru",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    }
}
@Composable
@Preview
fun PermainanScreenPreview(){
    PermainanScreen()
}


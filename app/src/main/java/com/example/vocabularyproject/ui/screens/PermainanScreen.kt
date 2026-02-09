package com.example.vocabularyproject.ui.screens


import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.vocabularyproject.ui.widgetstyles.HomeScreenButtonStyles
import com.example.vocabularyproject.util.buttonGradientClolor
import com.example.vocabularyproject.util.cardGradientColors
import com.example.vocabularyproject.viewmodels.InputWordViewModel
import com.example.vocabularyproject.R

@Composable
fun PermainanScreen(
    iWViewModel: InputWordViewModel = hiltViewModel()
) {
   //hide answer
    //when submit clicked show answer
    //fireworks if the answer correct?
    val currentItem by iWViewModel.currentItem.collectAsStateWithLifecycle()
    val cardIndex by iWViewModel.cardIndex.collectAsStateWithLifecycle()
    val answer by iWViewModel.answer.collectAsState()
    val isRevealed by iWViewModel.isRevealed.collectAsStateWithLifecycle()
    val answerText by iWViewModel.answerText.collectAsStateWithLifecycle()
    val isAnswerCorrect by iWViewModel.isAnswerCorrect.collectAsStateWithLifecycle()

    // 1. Setup the composition
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url("https://lottie.host/ceb39301-853c-4773-a3c3-c3f4b1a19d09/Fdo5XV3kic.lottie")
    )

// 2. Setup the animatable
    val lottieAnimatable = rememberLottieAnimatable()

// 3. Trigger the animation



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.2f)
    ) {
//        if (composition == null) {
//            Log.i("PermainanScreen","composition is null")
//        } else {
//            Log.i("PermainanScreen","composition exist")
//            LottieAnimation(
//                composition = composition,
//                iterations = LottieConstants.IterateForever,
//                modifier = Modifier.fillMaxSize()
//            )
//        }

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
                    .clickable { },
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
                        text = currentItem?.english?.eWord ?: "DONE",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = answer,
                        onValueChange = { newText ->iWViewModel.answer.value = newText }, // Update state when typing
                        label = { Text("Word") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HomeScreenButtonStyles(
                        "Submit",
                        onClick = {
                            iWViewModel.checkAnswer()
                            iWViewModel.toggleReveal()
                                  },
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = buttonGradientClolor
                    )


                    Text(
                        modifier = Modifier,
                        // Logic: If not revealed, repeat "●" for the length of the string
                        text = if (isRevealed) {
                            answerText
                        } else {
                            "●".repeat(answerText.length.coerceAtMost(15)) // Limit length so it doesn't look messy
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f)) // This pushes everything below it to the bottom
                    Text(
                        text = "Next",
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                iWViewModel.nextCard()
                                targetIndex
                            }
                    )
                }
            }
        }
        if (isAnswerCorrect) {
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .fillMaxSize() // This fills the whole screen outside the card
                    .pointerInput(Unit) { /* Optional: prevents clicking through fireworks */ },
                contentScale = ContentScale.FillBounds
            )
        }

        // LaunchedEffect can stay anywhere, but keeping it at the top is fine
        LaunchedEffect(isAnswerCorrect) {
            if (isAnswerCorrect) {
                lottieAnimatable.animate(composition = composition)
            }
        }
    }
}


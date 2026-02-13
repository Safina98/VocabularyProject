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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.font.FontStyle
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
import com.airbnb.lottie.compose.animateLottieCompositionAsState
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
    val lottieAnimatable = rememberLottieAnimatable()

    // 1. Setup the composition
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.code)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

// 3. Trigger the animation
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
                    .wrapContentHeight()
                    .padding(16.dp)
                    .clickable { },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()// Use fillMaxSize to cover the internal area of the card
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
                        text = if (isRevealed) {
                            answerText
                        } else {
                            "●".repeat(answerText.length.coerceAtMost(15)) // Limit length so it doesn't look messy
                        },
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
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
                    Spacer(modifier = Modifier.height(16.dp)) // This pushes everything below it to the bottom
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
        LaunchedEffect(isAnswerCorrect, composition) {
            if (isAnswerCorrect && composition != null) {
                lottieAnimatable.animate(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
            }
        }

        if (isAnswerCorrect && composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .pointerInput(Unit) {},
                contentScale = ContentScale.FillBounds
            )
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .pointerInput(Unit) {}
                    .align(Alignment.TopStart), // Align to top-left of the Box
                contentScale = ContentScale.FillBounds
            )

            // Top-right animation
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .align(Alignment.TopEnd)
                    .pointerInput(Unit) {},// Align to top-right of the Box
                contentScale = ContentScale.FillBounds
            )
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
                    .pointerInput(Unit) {}
                    .align(Alignment.BottomStart), // Align to top-left of the Box
                contentScale = ContentScale.FillBounds
            )

            // Top-right animation
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
                    .align(Alignment.BottomEnd)
                    .pointerInput(Unit) {},// Align to top-right of the Box
                contentScale = ContentScale.FillBounds
            )
        }
    }
}


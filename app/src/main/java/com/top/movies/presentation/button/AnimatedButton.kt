package com.top.movies.presentation.button

import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,

    ) {
    val animatable = remember { Animatable(1f) }
    val cs = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .then(modifier)
            .scale(animatable.value)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        cs.launch {
                            animatable.animateTo(
                                0.5f, animationSpec = tween(80)
                            )
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        cs.launch {
                            animatable.animateTo(
                                1f,
                                animationSpec = tween(80),
                                block = {
                                    if (animatable.value == 1f) {
                                        onClick.invoke()
                                    }
                                })
                        }
                    }
                }
                true
            }, contentAlignment = Alignment.Center
    ) {
        content.invoke()
    }
}
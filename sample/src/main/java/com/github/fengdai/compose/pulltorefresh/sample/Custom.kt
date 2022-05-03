package com.github.fengdai.compose.pulltorefresh.sample

import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.fengdai.compose.pulltorefresh.PullToRefresh
import com.github.fengdai.compose.pulltorefresh.PullToRefreshState
import com.github.fengdai.compose.pulltorefresh.rememberPullToRefreshState
import kotlinx.coroutines.delay

@Composable
fun Custom(navController: NavController) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Custom") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        var refreshing by remember { mutableStateOf(false) }
        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(1200)
                refreshing = false
            }
        }

        PullToRefresh(
            state = rememberPullToRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true },
            dragMultiplier = 1f,
            refreshTriggerDistance = 100.dp,
            refreshingOffset = 60.dp,
            indicatorPadding = padding,
            indicator = { state, refreshTriggerDistance, _ ->
                CustomPullToRefreshIndicator(refreshTriggerDistance, state)
            }
        ) {
            Content(padding)
        }
    }
}

@Composable
private fun CustomPullToRefreshIndicator(
    refreshTriggerDistance: Dp,
    state: PullToRefreshState
) {
    val refreshTriggerPx = with(LocalDensity.current) { refreshTriggerDistance.toPx() }
    val indicatorSize = 36.dp
    val indicatorHeightPx = with(LocalDensity.current) { indicatorSize.toPx() }
    val rotation: Float
    val scaleFraction: Float
    val alphaFraction: Float
    if (!state.isRefreshing) {
        val progress = (state.contentOffset / refreshTriggerPx.coerceAtLeast(1f))
            .coerceIn(0f, 1f)
        rotation = progress * 180
        scaleFraction = LinearOutSlowInEasing.transform(progress)
        alphaFraction = progress
    } else {
        val transition = rememberInfiniteTransition()
        rotation = transition.animateValue(
            0f,
            1f,
            Float.VectorConverter,
            infiniteRepeatable(
                animation = tween(
                    durationMillis = 1332, // 1 and 1/3 second
                    easing = LinearEasing
                )
            )
        ).value * 360
        scaleFraction = 1f
        alphaFraction = 1f
    }
    Image(
        painter = painterResource(id = R.drawable.custom),
        contentDescription = "refreshing",
        modifier = Modifier
            .graphicsLayer {
                translationY = (state.contentOffset - indicatorHeightPx) / 2f
                scaleX = scaleFraction
                scaleY = scaleFraction
                alpha = alphaFraction
            }
            .size(indicatorSize)
            .rotate(rotation)
    )
}

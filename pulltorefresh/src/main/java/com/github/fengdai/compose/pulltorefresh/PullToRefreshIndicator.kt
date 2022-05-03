package com.github.fengdai.compose.pulltorefresh

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PullToRefreshIndicator(
    state: PullToRefreshState,
    refreshTriggerDistance: Dp,
    refreshingOffset: Dp,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default.copy(color = Color.Gray, fontSize = 14.sp),
) {
    val refreshTriggerPx = with(LocalDensity.current) { refreshTriggerDistance.toPx() }
    val refreshingOffsetPx = with(LocalDensity.current) { refreshingOffset.toPx() }
    val indicatorHeight = 48.dp
    val indicatorHeightPx = with(LocalDensity.current) { indicatorHeight.toPx() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(indicatorHeight)
            .padding(end = 26.dp)
            .graphicsLayer {
                translationY = state.contentOffset - (refreshingOffsetPx + indicatorHeightPx) / 2
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.isRefreshing) {
            val transition = rememberInfiniteTransition()
            val progress by transition.animateValue(
                0f,
                1f,
                Float.VectorConverter,
                infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1332, // 1 and 1/3 second
                        easing = LinearEasing
                    )
                )
            )
            Image(
                painter = painterResource(id = R.drawable.refreshing),
                contentDescription = "refreshing",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp)
                    .rotate(progress * 360),
                colorFilter = ColorFilter.tint(textStyle.color)
            )
        } else {
            val progress = ((state.contentOffset - refreshTriggerPx / 2) / refreshTriggerPx * 2)
                .coerceIn(0f, 1f)
            Image(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "pull to refresh",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp)
                    .rotate(progress * 180),
                colorFilter = ColorFilter.tint(textStyle.color)
            )
        }
        BasicText(
            text = stringResource(
                when {
                    state.isPullInProgress && state.contentOffset >= refreshTriggerPx -> R.string.cptr_release_to_refresh
                    state.isRefreshing -> R.string.cptr_refreshing
                    else -> R.string.cptr_pull_to_refresh
                }
            ),
            style = textStyle
        )
    }
}

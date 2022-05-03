package com.github.fengdai.compose.pulltorefresh.sample

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavController
import com.github.fengdai.compose.pulltorefresh.PullToRefresh
import com.github.fengdai.compose.pulltorefresh.rememberPullToRefreshState
import kotlinx.coroutines.delay

@Composable
fun Basic(navController: NavController) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Basic") },
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
            indicatorPadding = padding
        ) {
            Content(padding)
        }
    }
}

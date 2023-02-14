package com.tt.imposteroslauncher.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun screenWidthInPx() =
    with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx() +
                WindowInsets.systemBars.getLeft(this, LayoutDirection.Ltr) +
                WindowInsets.systemBars.getRight(this, LayoutDirection.Ltr)
    }

@Composable
fun screenHeightInPx() =
    with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx() +
                WindowInsets.systemBars.getTop(this) +
                WindowInsets.systemBars.getBottom(this)
    }

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun Int.toPx() = with(LocalDensity.current) { this@toPx.dp.toPx() }

@Composable
fun Int.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }
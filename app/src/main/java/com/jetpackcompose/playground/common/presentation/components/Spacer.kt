package com.jetpackcompose.playground.common.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun spacer() {
    Spacer(
        modifier = Modifier
            .height(10.dp)
    )
}
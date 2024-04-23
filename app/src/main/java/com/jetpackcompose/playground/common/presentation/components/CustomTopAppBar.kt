package com.jetpackcompose.playground.common.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jetpackcompose.playground.main.presentation.data.CustomTopAppBarData

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTopAppBar(customTopAppBarData: CustomTopAppBarData) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(customTopAppBarData.title)
        },
        navigationIcon = {
            IconButton(onClick = customTopAppBarData.openIconClick) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "")
            }
        }
    )
}
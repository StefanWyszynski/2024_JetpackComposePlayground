package com.jetpackcompose.playground.common.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun <T> SearchResultListItem(
    resultItem: T,
    imageURL: String?,
    repoName: String?,
    onItemClick: (T) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            hoveredElevation = 5.dp, pressedElevation = 5.dp
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = {
            onItemClick(resultItem)
        }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageURL),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
            Text(
                text = repoName ?: "Empty",
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

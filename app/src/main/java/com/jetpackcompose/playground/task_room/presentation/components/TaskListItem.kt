package com.jetpackcompose.playground.task_room.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jetpackcompose.playground.task_room.domain.data.Priority
import com.jetpackcompose.playground.task_room.domain.data.Task

@Composable
fun TaskListItem(
    task: Task,
    onItemClick: () -> Unit,
    onDelete: () -> Unit
) {
    val cardColor = when (task.priority) {
        Priority.LOW -> Color.Green
        Priority.MEDIUM -> Color.Yellow
        Priority.HIGH -> Color.Red
    }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            hoveredElevation = 5.dp, pressedElevation = 5.dp
        ),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = {
            onItemClick()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = task.name ?: "Empty",
                    modifier = Modifier
                        .padding(8.dp)
                )
                Text(
                    text = task.date ?: "None",
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onDelete, modifier = Modifier.wrapContentSize()) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}

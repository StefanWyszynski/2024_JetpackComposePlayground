package com.jetpackcompose.playground.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun searchField(
    serachText: String,
    onSearchTextChange: (text: String) -> Unit
) {
    TextField(
        value = serachText,
        onValueChange = onSearchTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        textStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
        shape = RoundedCornerShape(14.dp),
        placeholder = {
        })
}

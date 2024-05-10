package com.jetpackcompose.playground.main.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.jetpackcompose.playground.main.presentation.data.NavigationDrawerItems

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 */
@Composable
fun DrawerContent(navController: NavController, onClickOptionCallback: () -> Unit) {

    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .background(
                color = Color(0xFF000077),
                shape = RoundedCornerShape(corner = CornerSize(0.dp))
            ),
        drawerShape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xFF000055),
                    shape = RoundedCornerShape(corner = CornerSize(0.dp))
                )
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = "Jetpack compose playground app",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(20.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
        val nav = NavOptions.Builder().setLaunchSingleTop(true)

        for (navigateItem in NavigationDrawerItems.entries) {
            DrawerContentOptionButton({
                navigateItem.navigate(navController, nav)
                onClickOptionCallback()
            }, stringResource(navigateItem.title))
        }
    }
}

@Composable
private fun DrawerContentOptionButton(
    onClickOption: () -> Unit, title: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovering by interactionSource.collectIsHoveredAsState()
    Button(modifier = Modifier
        .typicalButton(),
        colors = ButtonDefaults.buttonColors(
            contentColor = if (isHovering) Color(0xFFffffcc) else Color.White,
            containerColor = if (isHovering) Color(0xFFffffcc) else Color.White
        ),
        shape = RoundedCornerShape(4.dp),
        interactionSource = interactionSource,
        onClick = {
            onClickOption()
        }) {
        Text(text = title, color = Color.Black, fontSize = 10.sp)
    }
}

fun Modifier.typicalButton() = this
    .padding(4.dp)
    .fillMaxWidth(1f)
    .wrapContentHeight()

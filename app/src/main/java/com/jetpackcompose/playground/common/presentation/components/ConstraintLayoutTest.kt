package com.jetpackcompose.playground.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId

@Composable
fun ConstraintLayoutTest() {

    val constraintSet = ConstraintSet {
        val imageRef = createRefFor("Image")
        val buttonRef = createRefFor("Button")

        val guideline = createGuidelineFromStart(0.2f)
        constrain(imageRef) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(guideline)
            width = Dimension.fillToConstraints
            height = Dimension.value(100.dp)
        }
        constrain(buttonRef) {
            top.linkTo(parent.top)
            start.linkTo(guideline)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.value(100.dp)
        }
    }
    ConstraintLayout(constraintSet = constraintSet, modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Blue)
                .layoutId("Image")
        )
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Green)
                .layoutId("Button")
        )
    }
}

@Preview
@Composable
fun ConstarainTestPreview() {
    ConstraintLayoutTest()
}
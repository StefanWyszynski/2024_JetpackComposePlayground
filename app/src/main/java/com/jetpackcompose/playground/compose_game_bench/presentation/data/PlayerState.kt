package com.jetpackcompose.playground.compose_game_bench.presentation.data

data class PlayerState(
    var fov: Float = 60f,
    var halfFov: Float = 30f,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var angle: Double = 90.0,
    var health: Float = 100f,
    var moveSpeed: Double = 3.0,
    var rotateSpeed: Double = 70.0,
    val radius: Double = 5.0
) {
}
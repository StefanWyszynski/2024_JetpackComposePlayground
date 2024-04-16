package com.jetpackcompose.playground.compose_game_bench.data

data class RaycastScreenColumnInfo(
    var wallHeight: Double = 0.0,
    var colorIntensity: Int = 0,
    var xOffset: Int = 0,
    var worldTextureOffset: Double = 0.0,
    var hitWallNumber: Int = 0,
    var rayAngle: Double = 0.0
) {
}
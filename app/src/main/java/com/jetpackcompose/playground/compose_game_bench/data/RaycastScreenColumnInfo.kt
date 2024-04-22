package com.jetpackcompose.playground.compose_game_bench.data

data class RaycastScreenColumnInfo(
    var castedWallHeight: Double = 0.0,
    var castedWallColorIntensity: Int = 0,
    var virtualScreenXLineNumber: Int = 0,
    var worldTextureOffset: Double = 0.0,
    var eyeRayHitWallNumber: Int = 0,
    var rayAngle: Double = 0.0
) {
}
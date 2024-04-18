package com.jetpackcompose.playground.compose_game_bench.domain.usecase

import com.jetpackcompose.playground.compose_game_bench.data.Player
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.presentation.data.GameData

interface RayCastUseCase {
    fun rayCastingScreenColumnsInfo(
        screenColumns: List<RaycastScreenColumnInfo>,
        player: Player,
        gameData: GameData,
        textureWidth: Int,
        textureHeight: Int
    ): List<RaycastScreenColumnInfo>
}
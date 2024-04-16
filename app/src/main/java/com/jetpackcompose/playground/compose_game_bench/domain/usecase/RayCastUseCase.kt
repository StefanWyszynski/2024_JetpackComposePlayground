package com.jetpackcompose.playground.compose_game_bench.domain.usecase

import com.jetpackcompose.playground.compose_game_bench.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.presentation.data.GameData

interface RayCastUseCase {
    fun rayCastingScreenColumnsInfo(
        screenColumns: List<RaycastScreenColumnInfo>,
        playerState: PlayerState,
        gameData: GameData,
        textureWidth: Int,
        textureHeight: Int
    ): List<RaycastScreenColumnInfo>
}
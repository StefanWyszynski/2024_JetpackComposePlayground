package com.jetpackcompose.playground.compose_game_bench.domain.usecase

import com.jetpackcompose.playground.compose_game_bench.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState

interface RayCastUseCase {
    fun rayCastingScreenColumnsInfo(
        screenColumns: List<Int>,
        playerState: PlayerState,
        screenInfo: ScreenState,
        map: List<List<Int>>
    ): List<RaycastScreenColumnInfo>
}
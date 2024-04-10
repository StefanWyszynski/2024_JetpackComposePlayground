package com.jetpackcompose.playground.compose_game_bench.domain

import com.jetpackcompose.playground.compose_game_bench.data.PlayerState
import com.jetpackcompose.playground.compose_game_bench.data.ScreenState
import com.jetpackcompose.playground.compose_game_bench.domain.usecase.RayCastUseCase
import com.jetpackcompose.playground.compose_game_bench.domain.util.RayCastUtil
import javax.inject.Inject

class RayCastUseCaseImpl @Inject constructor(private val rayCastUtil: RayCastUtil) : RayCastUseCase {
    override fun rayCastingScreenColumnsInfo(
        screenColumns: List<Int>,
        playerState: PlayerState,
        screenInfo: ScreenState,
        map: List<List<Int>>
    ) = rayCastUtil.rayCastingScreenColumnsInfo(screenColumns, playerState, screenInfo, map)

}
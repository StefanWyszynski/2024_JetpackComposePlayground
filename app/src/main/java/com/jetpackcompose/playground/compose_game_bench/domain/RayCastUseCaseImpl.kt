package com.jetpackcompose.playground.compose_game_bench.domain

import com.jetpackcompose.playground.compose_game_bench.data.Player
import com.jetpackcompose.playground.compose_game_bench.data.RaycastScreenColumnInfo
import com.jetpackcompose.playground.compose_game_bench.domain.usecase.RayCastUseCase
import com.jetpackcompose.playground.compose_game_bench.domain.util.RayCastUtil
import com.jetpackcompose.playground.compose_game_bench.presentation.data.GameData
import javax.inject.Inject

class RayCastUseCaseImpl @Inject constructor(private val rayCastUtil: RayCastUtil) :
    RayCastUseCase {
    override fun rayCastingScreenColumnsInfo(
        screenColumns: List<RaycastScreenColumnInfo>,
        player: Player,
        gameData: GameData,
        textureWidth: Int,
        textureHeight: Int
    ) = rayCastUtil.rayCastingScreenColumnsInfo(
        screenColumns,
        player,
        gameData,
        textureWidth,
        textureHeight
    )

}
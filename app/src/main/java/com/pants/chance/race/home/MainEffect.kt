package com.pants.chance.race.home

import com.pants.chance.race.CreateEntrantResponse

sealed class MainEffect
data class CreateEntrant(val username: String) : MainEffect()
object Logout : MainEffect()
object GotoDistanceTravelled : MainEffect()
data class GotoLobby(val entrant: CreateEntrantResponse) : MainEffect()
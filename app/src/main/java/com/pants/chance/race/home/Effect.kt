package com.pants.chance.race.home

import com.pants.chance.race.CreateEntrantResponse

sealed class Effect
object CreateEntrant : Effect()
data class GotoLobby(val entrant: CreateEntrantResponse) : Effect()
package com.pants.chance.race

sealed class MainEffect
data class CreateEntrant(val username: String) : MainEffect()
object Logout : MainEffect()
object GotoDistanceTravelled : MainEffect()
data class GotoLobby(val entrant: CreateEntrantResponse) : MainEffect()

sealed class LobbyEffect
data class FetchTrack(val trackLink: String) : LobbyEffect()
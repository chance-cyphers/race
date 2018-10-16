package com.pants.chance.race

sealed class MainEvent
data class RacePressed(val username: String) : MainEvent()
object LogoutPressed : MainEvent()
object DistanceTravelledPressed : MainEvent()
data class EntrantCreated(val entrant: CreateEntrantResponse) : MainEvent()

sealed class LobbyEvent
data class TrackFetched(val track: Track) : LobbyEvent()
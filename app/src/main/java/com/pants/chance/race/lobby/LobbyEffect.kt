package com.pants.chance.race.lobby

sealed class LobbyEffect
data class FetchTrack(val trackLink: String) : LobbyEffect()
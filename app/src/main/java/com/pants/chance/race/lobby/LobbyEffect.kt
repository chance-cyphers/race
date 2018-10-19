package com.pants.chance.race.lobby

import com.pants.chance.race.Track

sealed class LobbyEffect
data class FetchTrack(val trackLink: String) : LobbyEffect()
data class FetchTrackWithDelay(val trackLink: String) : LobbyEffect()
data class GotoRace(val track: Track) : LobbyEffect()
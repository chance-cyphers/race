package com.pants.chance.race.lobby

import com.pants.chance.race.Track

sealed class LobbyEvent
data class TrackFetched(val track: Track, val trackLink: String) : LobbyEvent()
data class TrackFetchedError(val message: String) : LobbyEvent()
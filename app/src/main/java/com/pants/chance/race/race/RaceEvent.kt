package com.pants.chance.race.race

import com.pants.chance.race.Track

sealed class RaceEvent

data class LocationUpdateEvent(val lat: Double, val lon: Double, val timestamp: Long) : RaceEvent()
object PollTrackTick : RaceEvent()
data class TrackFetched(val track: Track) : RaceEvent()
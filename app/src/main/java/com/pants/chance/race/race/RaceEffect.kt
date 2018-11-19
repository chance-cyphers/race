package com.pants.chance.race.race

import com.pants.chance.race.Location

sealed class RaceEffect
data class UpdateLocationEffect(val locLink: String, val loc: Location) : RaceEffect()
data class FetchTrack(val trackLink: String) : RaceEffect()
data class GotoFinish(val winner: String) : RaceEffect()
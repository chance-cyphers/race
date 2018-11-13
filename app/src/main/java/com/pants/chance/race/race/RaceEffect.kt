package com.pants.chance.race.race

sealed class RaceEffect
data class UpdateLocation(val locLink: String, val lat: Double, val lon: Double) : RaceEffect()
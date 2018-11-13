package com.pants.chance.race.race

sealed class RaceEvent
data class LocationUpdateEvent(val lat: Double, val lon: Double, val timestamp: Long) : RaceEvent()
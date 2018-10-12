package com.pants.chance.race.home

import com.pants.chance.race.CreateEntrantResponse

sealed class Event
data class RacePressed(val username: String) : Event()
object LogoutPressed : Event()
object DistanceTravelledPressed : Event()
data class EntrantCreated(val entrant: CreateEntrantResponse) : Event()

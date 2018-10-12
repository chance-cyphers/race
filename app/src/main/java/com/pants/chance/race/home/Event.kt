package com.pants.chance.race.home

import com.pants.chance.race.CreateEntrantResponse

sealed class Event
object RacePressed : Event()
object LogoutPressed : Event()
object DistanceTravelledPressed : Event()
data class EntrantCreated(val entrant: CreateEntrantResponse) : Event()

package com.pants.chance.race.home

import com.pants.chance.race.CreateEntrantResponse

sealed class MainEvent
data class RacePressed(val username: String) : MainEvent()
object LogoutPressed : MainEvent()
data class EntrantCreated(val entrant: CreateEntrantResponse) : MainEvent()

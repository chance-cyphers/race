package com.pants.chance.race.home

import com.spotify.mobius.Effects
import com.spotify.mobius.Next

fun update(model: Int, event: Event): Next<Int, Effect> {
    return when (event) {
        RacePressed -> {
            Next.dispatch(Effects.effects(CreateEntrant))
        }
        is EntrantCreated -> {
            Next.dispatch(Effects.effects(GotoLobby(event.entrant)))
        }
    }
}
package com.pants.chance.race.home

import com.spotify.mobius.Effects
import com.spotify.mobius.Next

fun update(model: Int, event: Event): Next<Int, Effect> {
    return when (event) {
        is RacePressed -> Next.dispatch(Effects.effects(CreateEntrant(event.username)))
        is EntrantCreated -> Next.dispatch(Effects.effects(GotoLobby(event.entrant)))
        is LogoutPressed -> Next.dispatch(Effects.effects(Logout))
        DistanceTravelledPressed -> Next.dispatch(Effects.effects(GotoDistanceTravelled))
    }
}
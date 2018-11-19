package com.pants.chance.race.home

import com.spotify.mobius.Effects
import com.spotify.mobius.Next

fun update(model: MainModel, event: MainEvent): Next<MainModel, MainEffect> {
    return when (event) {
        is RacePressed -> Next.dispatch(Effects.effects(CreateEntrant(event.username)))
        is EntrantCreated -> Next.dispatch(Effects.effects(GotoLobby(event.entrant)))
        is LogoutPressed -> Next.dispatch(Effects.effects(Logout))
        is EntrantCreatedError -> Next.next(model.copy(error = event.message))
    }
}
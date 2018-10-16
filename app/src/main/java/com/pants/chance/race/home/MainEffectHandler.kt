package com.pants.chance.race.home

import com.pants.chance.race.*
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

fun createEffectHandler(
    gotoLobby: (String) -> Unit,
    logout: () -> Unit,
    gotoDistanceTravelled: () -> Unit
): (Consumer<MainEvent>) -> Connection<MainEffect> {

    return fun(eventConsumer: Consumer<MainEvent>): Connection<MainEffect> {
        return object : Connection<MainEffect> {
            override fun accept(effect: MainEffect) {
                when (effect) {
                    is CreateEntrant -> {
                        raceClient.createEntrant(CreateEntrantRequest(effect.username))
                            .map { it.body() ?: throw Exception("whoops") }
                            .subscribe { it ->
                                eventConsumer.accept(EntrantCreated(it))
                            }
                    }
                    is GotoLobby -> {
                        gotoLobby(effect.entrant.links.track)
                    }
                    is Logout -> logout()
                    is GotoDistanceTravelled -> gotoDistanceTravelled()
                }
            }

            override fun dispose() {}
        }
    }

}

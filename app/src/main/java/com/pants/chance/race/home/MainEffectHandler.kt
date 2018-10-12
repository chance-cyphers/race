package com.pants.chance.race.home

import com.pants.chance.race.CreateEntrantRequest
import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

fun createEffectHandler(
    gotoLobby: (String) -> Unit,
    logout: () -> Unit,
    gotoDistanceTravelled: () -> Unit
): (Consumer<Event>) -> Connection<Effect> {

    return fun(eventConsumer: Consumer<Event>): Connection<Effect> {
        return object : Connection<Effect> {
            override fun accept(effect: Effect) {
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

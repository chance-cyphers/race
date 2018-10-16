package com.pants.chance.race

import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

fun createEffectHandler(): (Consumer<LobbyEvent>) -> Connection<LobbyEffect> {

    return fun(eventConsumer: Consumer<LobbyEvent>): Connection<LobbyEffect> {
        return object : Connection<LobbyEffect> {
            override fun accept(effect: LobbyEffect) {
                when (effect) {
                    is FetchTrack -> {
                        raceClient.getTrack(effect.trackLink)
                            .map { it.body() ?: throw Exception("error fetching track") }
                            .subscribe { it -> eventConsumer.accept(TrackFetched(it))}
                    }
                }
            }

            override fun dispose() {}
        }
    }

}

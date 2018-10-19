package com.pants.chance.race.lobby

import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import java.util.concurrent.TimeUnit

fun createEffectHandler(gotoRace: (String) -> Unit): (Consumer<LobbyEvent>) -> Connection<LobbyEffect> {

    return fun(eventConsumer: Consumer<LobbyEvent>): Connection<LobbyEffect> {
        return object : Connection<LobbyEffect> {
            override fun accept(effect: LobbyEffect) {
                when (effect) {
                    is FetchTrack -> {
                        raceClient.getTrack(effect.trackLink)
                            .map { it.body() ?: throw Exception("error fetching track") }
                            .subscribe { it ->
                                eventConsumer.accept(TrackFetched(it, effect.trackLink))
                            }
                    }
                    is FetchTrackWithDelay -> {
                        raceClient.getTrack(effect.trackLink)
                            .delay(1, TimeUnit.SECONDS)
                            .map { it.body() ?: throw Exception("error fetching track") }
                            .subscribe { it ->
                                eventConsumer.accept(TrackFetched(it, effect.trackLink))
                            }
                    }
                    is GotoRace -> {
                        gotoRace("entrants: ${effect.track.entrants[0].userId}, ${effect.track.entrants[1].userId}")
                    }
                }
            }

            override fun dispose() {}
        }
    }
}
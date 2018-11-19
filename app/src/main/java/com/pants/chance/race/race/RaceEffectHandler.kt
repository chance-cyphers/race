package com.pants.chance.race.race

import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

fun createEffectHandler(gotoFinish: (String) -> Unit): (Consumer<RaceEvent>) -> Connection<RaceEffect> {

    return fun(eventConsumer: Consumer<RaceEvent>): Connection<RaceEffect> {
        return object : Connection<RaceEffect> {

            override fun accept(effect: RaceEffect) {
                when (effect) {
                    is UpdateLocationEffect -> {
                        raceClient.addLocation(effect.locLink, effect.loc)
                            .map { it.body() ?: throw Exception("error posting location") }
                            .subscribe({}, { /*ignore update errors for now*/ })
                    }
                    is FetchTrack -> {
                        raceClient.getTrack(effect.trackLink)
                            .map {
                                it.body() ?: throw Exception("error fetching track in race: $it")
                            }
                            .subscribe { it ->
                                eventConsumer.accept(TrackFetched(it))
                            }
                    }
                    is GotoFinish -> {
                        gotoFinish(effect.winner)
                    }
                }
            }

            override fun dispose() {}
        }
    }

}
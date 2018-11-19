package com.pants.chance.race.race

import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

fun createEffectHandler(gotoFinish: (String) -> Unit): (Consumer<RaceEvent>) -> Connection<RaceEffect> {

    return fun(eventConsumer: Consumer<RaceEvent>): Connection<RaceEffect> {
        return object : Connection<RaceEffect> {

            val compositeDisposable = CompositeDisposable()

            override fun accept(effect: RaceEffect) {
                when (effect) {
                    is UpdateLocationEffect -> {
                        raceClient.addLocation(effect.locLink, effect.loc)
                            .map { it.body() ?: throw Exception("error posting location") }
                            .subscribe({}, { /*ignore loc update errors for now*/ })
                            .addTo(compositeDisposable)
                    }
                    is FetchTrack -> {
                        raceClient.getTrack(effect.trackLink)
                            .map {
                                it.body() ?: throw Exception("error fetching track in race: $it")
                            }
                            .subscribe({ it ->
                                eventConsumer.accept(TrackFetched(it))
                            }, {
                                eventConsumer.accept(TrackFetchError(it.localizedMessage))
                            })
                            .addTo(compositeDisposable)

                    }
                    is GotoFinish -> {
                        gotoFinish(effect.winner)
                    }
                }
            }

            override fun dispose() {
                compositeDisposable.clear()
            }
        }
    }

}
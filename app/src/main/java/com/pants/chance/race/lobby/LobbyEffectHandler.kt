package com.pants.chance.race.lobby

import com.pants.chance.race.Track
import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

fun createEffectHandler(gotoRace: (Track) -> Unit): (Consumer<LobbyEvent>) -> Connection<LobbyEffect> {

    return fun(eventConsumer: Consumer<LobbyEvent>): Connection<LobbyEffect> {
        return object : Connection<LobbyEffect> {

            val compositeDisposable = CompositeDisposable()

            override fun accept(effect: LobbyEffect) {
                when (effect) {
                    is FetchTrack -> {
                        raceClient.getTrack(effect.trackLink)
                            .map { it.body() ?: throw Exception("error fetching track") }
                            .subscribe ({ it ->
                                eventConsumer.accept(TrackFetched(it, effect.trackLink))
                            }, {
                                eventConsumer.accept(TrackFetchedError(it.localizedMessage))
                            })
                            .addTo(compositeDisposable)
                    }
                    is FetchTrackWithDelay -> {
                        raceClient.getTrack(effect.trackLink)
                            .delay(1, TimeUnit.SECONDS)
                            .map { it.body() ?: throw Exception("error fetching track") }
                            .subscribe ({ it ->
                                eventConsumer.accept(TrackFetched(it, effect.trackLink))
                            }, {
                                eventConsumer.accept(TrackFetchedError(it.localizedMessage))
                            })
                            .addTo(compositeDisposable)
                    }
                    is GotoRace -> {
                        gotoRace(effect.track)
                    }
                }
            }

            override fun dispose() { compositeDisposable.clear() }
        }
    }
}
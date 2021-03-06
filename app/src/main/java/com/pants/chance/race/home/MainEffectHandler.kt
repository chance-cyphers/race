package com.pants.chance.race.home

import com.pants.chance.race.CreateEntrantRequest
import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

fun createEffectHandler(
    gotoLobby: (String) -> Unit,
    logout: () -> Unit
): (Consumer<MainEvent>) -> Connection<MainEffect> {

    return fun(eventConsumer: Consumer<MainEvent>): Connection<MainEffect> {
        return object : Connection<MainEffect> {

            val compositeDisposable = CompositeDisposable()

            override fun accept(effect: MainEffect) {
                when (effect) {
                    is CreateEntrant -> {
                        raceClient.createEntrant(CreateEntrantRequest(effect.username))
                            .map { it.body() ?: throw Exception("error creating entrant") }
                            .subscribe ({ it ->
                                eventConsumer.accept(EntrantCreated(it))
                            }, {
                                eventConsumer.accept(EntrantCreatedError(it.localizedMessage))
                            })
                            .addTo(compositeDisposable)
                    }
                    is GotoLobby -> {
                        gotoLobby(effect.entrant.links.track)
                    }
                    is Logout -> logout()
                }
            }

            override fun dispose() { compositeDisposable.clear() }
        }
    }

}

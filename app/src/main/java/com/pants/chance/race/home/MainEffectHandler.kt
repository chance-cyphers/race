package com.pants.chance.race.home

import android.util.Log
import com.pants.chance.race.CreateEntrantRequest
import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

fun createEffectHandler(gotoLobby: (String) -> Unit): (Consumer<Event>) -> Connection<Effect> {
    Log.i("qwerty", "creating effect handler")

    return fun(eventConsumer: Consumer<Event>): Connection<Effect> {
        return object : Connection<Effect> {
            override fun accept(effect: Effect) {
                when (effect) {
                    CreateEntrant -> {
                        Log.i("qwerty", "creating entrant...")

                        raceClient.createEntrant(CreateEntrantRequest("bob francis"))
                            .map { it.body() ?: throw Exception("whoops") }
                            .subscribe { it ->
                                eventConsumer.accept(EntrantCreated(it))
                            }
                    }
                    is GotoLobby -> {
                        gotoLobby(effect.entrant.links.track)
                    }
                }
            }

            override fun dispose() {}
        }
    }

}

package com.pants.chance.race.race

import com.pants.chance.race.raceClient
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

fun createEffectHandler(): (Consumer<RaceEvent>) -> Connection<RaceEffect> {

    return fun(_: Consumer<RaceEvent>): Connection<RaceEffect> {
        return object : Connection<RaceEffect> {

            override fun accept(effect: RaceEffect) {
                when (effect) {
                    is UpdateLocationEffect -> {
                        raceClient.addLocation(effect.locLink, effect.loc)
                            .map { it.body() ?: throw Exception("error posting location") }
                            .subscribe()
                    }
                }
            }

            override fun dispose() {}
        }
    }

}
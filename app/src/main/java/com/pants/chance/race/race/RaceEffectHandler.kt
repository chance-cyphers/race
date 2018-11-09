package com.pants.chance.race.race

import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

fun createEffectHandler(): (Consumer<RaceEvent>) -> Connection<RaceEffect> {

    return fun(eventConsumer: Consumer<RaceEvent>): Connection<RaceEffect> {
        return object : Connection<RaceEffect> {

            override fun accept(value: RaceEffect?) {
            }

            override fun dispose() {}
        }
    }

}
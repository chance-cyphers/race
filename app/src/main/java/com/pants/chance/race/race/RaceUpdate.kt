package com.pants.chance.race.race

import com.spotify.mobius.Next

fun update(model: RaceModel, event: RaceEvent) : Next<RaceModel, RaceEffect>{
    return Next.noChange()
}
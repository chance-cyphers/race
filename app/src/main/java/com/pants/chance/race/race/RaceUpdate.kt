package com.pants.chance.race.race

import com.spotify.mobius.Effects
import com.spotify.mobius.First
import com.spotify.mobius.Next

fun update(model: RaceModel, event: RaceEvent) : Next<RaceModel, RaceEffect>{
    return Next.noChange()
}

fun init(model: RaceModel): First<RaceModel, RaceEffect> {
    val updateLoc: RaceEffect = UpdateLocation(model.locLink, 12.0, 32.1)
    return First.first(model, Effects.effects(updateLoc))
}
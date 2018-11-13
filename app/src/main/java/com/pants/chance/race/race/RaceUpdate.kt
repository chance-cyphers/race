package com.pants.chance.race.race

import com.pants.chance.race.Location
import com.spotify.mobius.Effects
import com.spotify.mobius.First
import com.spotify.mobius.Next

fun update(model: RaceModel, event: RaceEvent) : Next<RaceModel, RaceEffect>{
    when (event) {
        is LocationUpdateEvent -> {
            return Next.next(RaceModel(model.locLink, Location(987, event.lat, event.lon)))
        }
    }
}

//fun init(model: RaceModel): First<RaceModel, RaceEffect> {
//    val updateLoc: RaceEffect = UpdateLocation(model.locLink, 12.0, 32.1)
//    return First.first(model, Effects.effects(updateLoc))
//}
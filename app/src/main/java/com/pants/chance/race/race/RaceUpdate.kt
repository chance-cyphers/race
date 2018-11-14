package com.pants.chance.race.race

import com.pants.chance.race.Location
import com.spotify.mobius.Effects
import com.spotify.mobius.Next

fun update(model: RaceModel, event: RaceEvent) : Next<RaceModel, RaceEffect>{
    when (event) {
        is LocationUpdateEvent -> {
            val location = Location(event.timestamp, event.lat, event.lon)
            val updateLocEffect = UpdateLocationEffect(model.locLink, location)
            return Next.next(model.copy(lastLoc = location), Effects.effects(updateLocEffect))
        }
        is PollTrackTick -> {
            return if (model.lastLoc != null ){
                Next.dispatch(Effects.effects(FetchTrack(model.locLink)))
            } else {
                Next.noChange()
            }
        }
    }
}
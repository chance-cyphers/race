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
                Next.dispatch(Effects.effects(FetchTrack(model.trackLink)))
            } else {
                Next.noChange()
            }
        }

        is TrackFetched -> {
            val entrant1 = event.track.entrants[0]
            val entrant2 = event.track.entrants[1]

            val distance1 = "${entrant1.userId}: ${entrant1.distance} km"
            val distance2 = "${entrant2.userId}: ${entrant2.distance} km"

            val updated = model.copy(distance1 = distance1, distance2 = distance2)
            return Next.next(updated)
        }
    }
}
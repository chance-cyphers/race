package com.pants.chance.race.race

import com.pants.chance.race.Location
import com.spotify.mobius.Effects
import com.spotify.mobius.Next

fun update(model: RaceModel, event: RaceEvent): Next<RaceModel, RaceEffect> {
    when (event) {
        is LocationUpdateEvent -> {
            val location = Location(event.timestamp, event.lat, event.lon)
            val updateLocEffect = UpdateLocationEffect(model.locLink, location)
            return Next.next(model.copy(lastLoc = location), Effects.effects(updateLocEffect))
        }

        is PollTrackTick -> {
            return Next.dispatch(Effects.effects(FetchTrack(model.trackLink)))
        }

        is TrackFetched -> {
            val entrant1 = event.track.entrants[0]
            val entrant2 = event.track.entrants[1]

            val label1 = "${entrant1.userId}: ${String.format("%.2f", entrant1.distance)} km"
            val label2 = "${entrant2.userId}: ${String.format("%.2f", entrant2.distance)} km"

            val progress1 = (entrant1.distance * 100).toInt()
            val progress2 = (entrant2.distance * 100).toInt()

            val updated = model.copy(
                label1 = label1,
                label2 = label2,
                error = null,
                progress1 = progress1,
                progress2 = progress2
            )

            return if (event.track.status == "finished" && event.track.winner != null) {
                Next.next(updated, Effects.effects(GotoFinish(event.track.winner)))
            } else {
                Next.next(updated)
            }
        }

        is TrackFetchError -> {
            return Next.next(model.copy(error = "Error: ${event.message}"))
        }
    }
}
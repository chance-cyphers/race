package com.pants.chance.race.lobby

import com.spotify.mobius.Effects
import com.spotify.mobius.First
import com.spotify.mobius.Next

fun update(model: LobbyModel, event: LobbyEvent): Next<LobbyModel, LobbyEffect> {
    return when (event) {
        is TrackFetched -> {
            if (event.track.status == "started") {
                val gotoRace: LobbyEffect = GotoRace(event.track)
                Next.dispatch(Effects.effects(gotoRace))
            } else {
                val fetchTrackWithDelay: LobbyEffect = FetchTrackWithDelay(event.trackLink)
                Next.dispatch(Effects.effects(fetchTrackWithDelay))
            }
        }
        is TrackFetchedError -> Next.next(model.copy(error = event.message))
    }
}

fun init(model: LobbyModel, trackLink: String): First<LobbyModel, LobbyEffect> {
    val fetchTrack: LobbyEffect = FetchTrack(trackLink)
    return First.first(model, Effects.effects(fetchTrack))
}
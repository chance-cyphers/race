package com.pants.chance.race.race

import com.spotify.mobius.EventSource
import com.spotify.mobius.disposables.Disposable
import com.spotify.mobius.functions.Consumer
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class TimerEventSource(private val interval: Long) : EventSource<RaceEvent> {

    override fun subscribe(eventConsumer: Consumer<RaceEvent>?): Disposable {
        val timer = Timer()
        timer.scheduleAtFixedRate(0, interval) {
            eventConsumer?.accept(PollTrackTick)
        }
        return Disposable { timer.cancel() }
    }

}
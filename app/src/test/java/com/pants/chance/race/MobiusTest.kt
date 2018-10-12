package com.pants.chance.race

import com.spotify.mobius.Connection
import com.spotify.mobius.Effects.effects
import com.spotify.mobius.First
import com.spotify.mobius.Mobius
import com.spotify.mobius.Next
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import org.junit.Test


class MobiusTest {

    @Test
    fun mobius_androidStuff() {
        val loopBuilder = Mobius.loop(this::update, this::effectHandler)
            .init { First.first(it) }
        val controller = MobiusAndroid.controller(loopBuilder, 23)

        controller.connect(this::connectViews)
    }


    private fun connectViews(eventConsumer: Consumer<Event>): Connection<Int> {
        // send events to the consumer when the button is pressed
//        button.setOnClickListener { view -> eventConsumer.accept(MyEvent.buttonPressed()) }

        return object : Connection<Int> {
            override fun accept(model: Int) {
                println("it's almost like i'm displaying the number $model in a text box right now")
            }

            override fun dispose() {
                // don't forget to remove listeners when the UI is disconnected
            }
        }
    }

    @Test
    fun mobius_doesThings() {
        val loop = Mobius.loop(this::update, this::effectHandler).startFrom(2)

        loop.observe { counter ->
            println("Num num: $counter")
        }

        loop.dispatchEvent(Event.UP)
        loop.dispatchEvent(Event.UP)
        loop.dispatchEvent(Event.DOWN)
        loop.dispatchEvent(Event.DOWN)
        loop.dispatchEvent(Event.DOWN)
        loop.dispatchEvent(Event.DOWN)
        loop.dispatchEvent(Event.DOWN)
        loop.dispatchEvent(Event.DOWN)

        Thread.sleep(500)
        loop.dispose()
    }

    private fun update(model: Int, event: Event): Next<Int, Effect> {
        return when (event) {
            Event.UP -> Next.next(model + 1)
            Event.DOWN -> {
                return if (model > 0) Next.next(model - 1)
                else Next.dispatch(effects(Effect.REPORT_ERROR_NEGATIVE))
            }
        }
    }

    private fun effectHandler(eventConsumer: Consumer<Event>): Connection<Effect> {
        return object : Connection<Effect> {
            override fun accept(effect: Effect) {
                if (effect == Effect.REPORT_ERROR_NEGATIVE) println("error!")
            }
            override fun dispose() {}
        }
    }

}

enum class Event {
    UP, DOWN
}

enum class Effect {
    REPORT_ERROR_NEGATIVE
}

package com.pants.chance.race

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.spotify.mobius.*
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.UpdateManager

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var controller: MobiusLoop.Controller<Int, Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutButton.setOnClickListener { logout() }

        distanceTravelledButton.setOnClickListener {
            startActivity(Intent(this, DistanceTravelledActivity::class.java))
        }

        setupRaceButton()
        checkForUpdates()
    }

    private fun setupRaceButton() {
        val loopBuilder = Mobius.loop(this::update, this::effectHandler)
            .init { First.first(it) }
//            .eventSource()
        controller = MobiusAndroid.controller(loopBuilder, 23)
        controller.connect(this::connectViews)
    }

    private fun connectViews(eventConsumer: Consumer<Event>): Connection<Int> {
        raceButton.setOnClickListener { eventConsumer.accept(RacePressed) }

        return object : Connection<Int> {
            override fun accept(model: Int) {
                Log.i("qwerty", "accepting event with model: $model")
            }

            override fun dispose() {
                raceButton.setOnClickListener(null)
            }
        }
    }

    private fun update(model: Int, event: Event): Next<Int, Effect> {
        return when (event) {
            Up -> Next.next(model + 1)
            Down -> {
                return if (model > 0) Next.next(model - 1)
                else Next.dispatch(Effects.effects(ReportErrorNegative))
            }
            RacePressed -> {
                return Next.dispatch(Effects.effects(CreateEntrant))
            }
            is EntrantCreated -> {
                return Next.dispatch(Effects.effects(
                    GotoLobby(event.entrant, Intent(this, LobbyActivity::class.java))))
            }
        }
    }

    private fun effectHandler(eventConsumer: Consumer<Event>): Connection<Effect> {
        return object : Connection<Effect> {
            override fun accept(effect: Effect) {
                when (effect) {
                    ReportErrorNegative -> println("error!")
                    CreateEntrant -> {
                        raceClient.createEntrant(CreateEntrantRequest("bob francis"))
                            .map { it.body() ?: throw Exception("whoops") }
                            .subscribe { it ->
                                eventConsumer.accept(EntrantCreated(it))
                            }
                    }
                    is GotoLobby -> {
                        effect.intent.putExtra("trackLink", effect.entrant.links.track)
                        startActivity(effect.intent)
                    }
                }
            }

            override fun dispose() {}
        }
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(LoginActivity.KEY_CLEAR_CREDENTIALS, true)
        startActivity(intent)
        finish()
    }

    public override fun onResume() {
        super.onResume()
        checkForCrashes()
        controller.start()
    }

    public override fun onPause() {
        super.onPause()
        unregisterManagers()
        controller.stop()
    }

    private fun checkForCrashes() {
        CrashManager.register(this)
    }

    private fun checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this)
    }

    public override fun onDestroy() {
        super.onDestroy()
        unregisterManagers()
        compositeDisposable.clear()
    }

    private fun unregisterManagers() {
        UpdateManager.unregister()
    }

}

sealed class Event
object Up : Event()
object Down : Event()
object RacePressed : Event()
data class EntrantCreated(val entrant: CreateEntrantResponse) : Event()

sealed class Effect
object ReportErrorNegative : Effect()
object CreateEntrant : Effect()
data class GotoLobby(val entrant: CreateEntrantResponse, val intent: Intent) : Effect()
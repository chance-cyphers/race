package com.pants.chance.race.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.*
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
        val loopBuilder = Mobius.loop(::update, createEffectHandler(this::gotoLobby))
            .init { First.first(it) }
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

    private fun gotoLobby(trackLink: String) {
        val lobbyIntent = Intent(this, LobbyActivity::class.java)
        lobbyIntent.putExtra("trackLink", trackLink)
        startActivity(lobbyIntent)
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
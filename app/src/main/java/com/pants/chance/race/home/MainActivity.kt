package com.pants.chance.race.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.*
import com.pants.chance.race.LoginActivity.Companion.EXTRA_ACCESS_TOKEN
import com.pants.chance.race.LoginActivity.Companion.EXTRA_ID_TOKEN
import com.spotify.mobius.Connection
import com.spotify.mobius.First
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.UpdateManager

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var controller: MobiusLoop.Controller<Int, Event>
    private lateinit var idToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idToken = intent.getStringExtra(EXTRA_ID_TOKEN)

        val loopBuilder = Mobius.loop(
            ::update,
            createEffectHandler(this::gotoLobby, this::logout, this::gotoDistanceTravelled)
        ).init { First.first(it) }

        controller = MobiusAndroid.controller(loopBuilder, 23)
        controller.connect(this::connectViews)

        checkForUpdates()
    }

    private fun connectViews(eventConsumer: Consumer<Event>): Connection<Int> {
        distanceTravelledButton.setOnClickListener { eventConsumer.accept(DistanceTravelledPressed) }
        raceButton.setOnClickListener { eventConsumer.accept(RacePressed(getName(idToken).orEmpty())) }
        logoutButton.setOnClickListener { eventConsumer.accept(LogoutPressed) }

        return object : Connection<Int> {
            override fun accept(model: Int) {
                Log.i("qwerty", "accepting event with model: $model")
            }

            override fun dispose() {
                raceButton.setOnClickListener(null)
                distanceTravelledButton.setOnClickListener(null)
                logoutButton.setOnClickListener(null)
            }
        }
    }

    private fun gotoDistanceTravelled() {
        startActivity(Intent(this, DistanceTravelledActivity::class.java))
    }

    private fun gotoLobby(trackLink: String) {
        val lobbyIntent = Intent(this, LobbyActivity::class.java)
        lobbyIntent.putExtra("trackLink", trackLink)
        lobbyIntent.putExtra("name", getName(idToken))
        startActivity(lobbyIntent)
    }

    private fun logout() {
        val logoutIntent = Intent(this, LoginActivity::class.java)
        logoutIntent.putExtra(LoginActivity.KEY_CLEAR_CREDENTIALS, true)
        startActivity(logoutIntent)
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
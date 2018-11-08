package com.pants.chance.race.lobby

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.R
import com.pants.chance.race.Track
import com.pants.chance.race.race.RaceActivity
import com.spotify.mobius.Connection
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : AppCompatActivity() {

    companion object {
        const val LOCATION_LINK = "locationLink"
    }

    private lateinit var controller: MobiusLoop.Controller<String, LobbyEvent>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        val trackLink = intent.getStringExtra("trackLink")

        val loopBuilder =
            Mobius.loop(::update, createEffectHandler(this::gotoRace))
                .init { init(it, trackLink) }
        controller = MobiusAndroid.controller(loopBuilder, "finding match...")
        controller.connect(this::connectViews)
    }

    private fun connectViews(eventConsumer: Consumer<LobbyEvent>): Connection<String> {

        return object : Connection<String> {
            override fun accept(model: String) {
                lobbyText.text = model
            }

            override fun dispose() {}
        }
    }



    private fun gotoRace(track: Track) {
        val gotoRaceIntent = Intent(this, RaceActivity::class.java)
        gotoRaceIntent.putExtra(LOCATION_LINK, track.links.locationUpdate)

        startActivity(gotoRaceIntent)
        finish()
    }

    public override fun onResume() {
        super.onResume()
        controller.start()
    }

    public override fun onPause() {
        super.onPause()
        controller.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        controller.disconnect()
    }

}

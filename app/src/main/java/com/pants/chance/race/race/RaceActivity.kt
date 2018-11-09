package com.pants.chance.race.race

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.R
import com.pants.chance.race.lobby.LobbyActivity
import com.spotify.mobius.Connection
import com.spotify.mobius.First
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import kotlinx.android.synthetic.main.activity_race.*

class RaceActivity : AppCompatActivity() {

    private lateinit var controller: MobiusLoop.Controller<RaceModel, RaceEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)


        val loopBuilder = Mobius.loop(::update, createEffectHandler()).init {
            First.first(it)
        }

        val locLink = intent.getStringExtra(LobbyActivity.LOCATION_LINK)
        controller = MobiusAndroid.controller(loopBuilder, RaceModel(locLink))
        controller.connect(this::connectViews)
    }

    private fun connectViews(eventConsumer: Consumer<RaceEvent>): Connection<RaceModel> {

        return object : Connection<RaceModel> {
            override fun accept(model: RaceModel) {
                raceText.text = "loc link: ${model.locLink}"
            }

            override fun dispose() {}
        }
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

data class RaceModel(val locLink: String)
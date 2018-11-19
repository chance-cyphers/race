package com.pants.chance.race.race

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.Location
import com.pants.chance.race.R
import com.pants.chance.race.finish.FinishActivity
import com.pants.chance.race.lobby.LobbyActivity
import com.spotify.mobius.Connection
import com.spotify.mobius.First
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import kotlinx.android.synthetic.main.activity_race.*

class RaceActivity : AppCompatActivity() {

    companion object {
        const val WINNER = "winner"
    }

    private lateinit var controller: MobiusLoop.Controller<RaceModel, RaceEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)

        val loopBuilder = Mobius
            .loop(::update, createEffectHandler(this::gotoFinish))
            .init { First.first(it) }
            .eventSources(
                LocationEventSource(this),
                TimerEventSource(5000)
            )

        val locLink = intent.getStringExtra(LobbyActivity.LOCATION_LINK)
        val trackLink = intent.getStringExtra(LobbyActivity.TRACK_LINK)

        controller = MobiusAndroid.controller(loopBuilder, RaceModel(locLink, trackLink))
        controller.connect(this::connectViews)
    }

    private fun connectViews(eventConsumer: Consumer<RaceEvent>): Connection<RaceModel> {

        return object : Connection<RaceModel> {
            override fun accept(model: RaceModel) {
                entrant1Text.text = model.label1
                entrant2Text.text = model.label2
                progressBar1.progress = model.progress1
                progressBar2.progress = model.progress2

                if (model.error != null) {
                    Toast.makeText(applicationContext, model.error, Toast.LENGTH_SHORT).show()
                }
            }

            override fun dispose() {}
        }
    }

    private fun gotoFinish(winner: String) {
        val gotoFinishIntent = Intent(this, FinishActivity::class.java)
        gotoFinishIntent.putExtra(WINNER, winner)
        startActivity(gotoFinishIntent)
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

data class RaceModel(
    val locLink: String,
    val trackLink: String,
    val lastLoc: Location? = null,
    val label1: String? = null,
    val label2: String? = null,
    val error: String? = null,
    val progress1: Int = 0,
    val progress2: Int = 0
)
package com.pants.chance.race.lobby

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.R
import com.spotify.mobius.Connection
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var controller: MobiusLoop.Controller<String, LobbyEvent>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        val trackLink = intent.getStringExtra("trackLink")

        val loopBuilder =
            Mobius.loop(::update, createEffectHandler())
                .init { init(it, trackLink) }
        controller = MobiusAndroid.controller(loopBuilder, "fetching track...")
        controller.connect(this::connectViews)
    }

    private fun connectViews(eventConsumer: Consumer<LobbyEvent>): Connection<String> {

        return object : Connection<String> {
            override fun accept(model: String) {
                lobbyText.text = "your track: $model"
            }

            override fun dispose() {
            }
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
        compositeDisposable.clear()
    }

}

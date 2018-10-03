package com.pants.chance.race

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        val trackLink = intent.getStringExtra("trackLink")
        lobbyText.text = "fetching track..."

        raceClient.getTrack("https://$trackLink")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                lobbyText.text = "your track: ${it.body().toString()}" }
            .addTo(compositeDisposable)
    }

    public override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}

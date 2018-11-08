package com.pants.chance.race.race

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.R
import com.pants.chance.race.lobby.LobbyActivity
import kotlinx.android.synthetic.main.activity_race.*

class RaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)

        val locLink = intent.getStringExtra(LobbyActivity.LOCATION_LINK)

        raceText.text = "here it is: $locLink!"
    }

}

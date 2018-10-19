package com.pants.chance.race.race

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pants.chance.race.R
import kotlinx.android.synthetic.main.activity_race.*

class RaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)

        val placeholder = intent.getStringExtra("placeholder")

        raceText.text = placeholder
    }

}

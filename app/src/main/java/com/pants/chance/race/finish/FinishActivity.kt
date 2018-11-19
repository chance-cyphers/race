package com.pants.chance.race.finish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pants.chance.race.R
import com.pants.chance.race.race.RaceActivity.Companion.WINNER
import kotlinx.android.synthetic.main.activity_finish.*

class FinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        val winner = intent.getStringExtra(WINNER)

        finishText.text = "And the winner is: $winner"
    }
}

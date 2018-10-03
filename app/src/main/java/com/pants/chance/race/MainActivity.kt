package com.pants.chance.race

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.UpdateManager

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

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
        val clicks = raceButton.clicks()
        clicks
            .flatMapSingle {
                raceClient.createEntrant(CreateEntrantRequest("bob francis"))
            }
            .map { it.body() ?: throw Exception("whoops") }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                val intent = Intent(this, LobbyActivity::class.java)
                intent.putExtra("trackLink", it.links.track)
                startActivity(intent)
            }, {
                error(it)
            })
            .addTo(compositeDisposable)
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
    }

    public override fun onPause() {
        super.onPause()
        unregisterManagers()
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

//val retrofit = Retrofit.Builder()
//    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
//    .addConverterFactory(MoshiConverterFactory.create())
//    .baseUrl("https://race-apu.herokuapp.com")
//    .build()
//
//val raceClient = retrofit.create(RaceClient::class.java)

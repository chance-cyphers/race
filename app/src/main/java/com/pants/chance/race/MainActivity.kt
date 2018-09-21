package com.pants.chance.race

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.UpdateManager
import net.hockeyapp.android.CrashManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutButton.setOnClickListener { logout() }

        distanceTravelledButton.setOnClickListener {
            startActivity(Intent(this, DistanceTravelledActivity::class.java))
        }

        raceButton.setOnClickListener {
            startActivity(Intent(this, LobbyActivity::class.java))
        }

        checkForUpdates()
    }

    public override fun onResume() {
        super.onResume()
        checkForCrashes()
    }

    public override fun onPause() {
        super.onPause()
        unregisterManagers()
    }

    public override fun onDestroy() {
        super.onDestroy()
        unregisterManagers()
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(LoginActivity.KEY_CLEAR_CREDENTIALS, true)
        startActivity(intent)
        finish()
    }

    private fun checkForCrashes() {
        CrashManager.register(this)
    }

    private fun checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this)
    }

    private fun unregisterManagers() {
        UpdateManager.unregister()
    }

}

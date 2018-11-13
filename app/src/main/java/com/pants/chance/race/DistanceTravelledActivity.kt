package com.pants.chance.race

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.distance_travelled.*

class DistanceTravelledActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.distance_travelled)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                locationResult.locations.forEach { Trip.record(it) }
                distanceText.text = """dist travelled: ${Trip.getDistance()}"""
            }
        }

        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null)
    }

    public override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    private fun createLocationRequest(): LocationRequest {
        return LocationRequest().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun checkPermission() {
        val hasntPermissions = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

        if (hasntPermissions) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123
            )
        }
    }
}
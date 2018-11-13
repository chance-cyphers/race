package com.pants.chance.race.race

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.spotify.mobius.EventSource
import com.spotify.mobius.disposables.Disposable
import com.spotify.mobius.functions.Consumer

class LocationEventSource(private val activity: Activity) : EventSource<RaceEvent> {

    override fun subscribe(eventConsumer: Consumer<RaceEvent>?): Disposable {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        checkPermission()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                Log.i("qwerty", "loc result: ${locationResult.lastLocation}")
//                eventConsumer?.accept(
//                    LocationUpdateEvent(
//                        locationResult.lastLocation.latitude,
//                        locationResult.lastLocation.longitude
//                    )
//                )
            }
        }

        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null)

        return Disposable {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
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
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

        if (hasntPermissions) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123
            )
        }
    }
}
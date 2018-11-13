package com.pants.chance.race.race

import android.app.Activity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.pants.chance.race.util.checkPermission
import com.spotify.mobius.EventSource
import com.spotify.mobius.disposables.Disposable
import com.spotify.mobius.functions.Consumer

class LocationEventSource(private val activity: Activity) : EventSource<RaceEvent> {

    override fun subscribe(eventConsumer: Consumer<RaceEvent>?): Disposable {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, activity)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                val lastLocation = locationResult.lastLocation
                eventConsumer?.accept(
                    LocationUpdateEvent(
                        lastLocation.latitude,
                        lastLocation.longitude,
                        System.currentTimeMillis()
                    )
                )
            }
        }

        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null)

        return Disposable {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest().apply {
            interval = 2000
            fastestInterval = 100
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

}
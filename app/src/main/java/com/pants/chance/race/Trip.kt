package com.pants.chance.race

import android.location.Location

object Trip {

    private val coords : MutableList<Pair<Double, Double>> = mutableListOf()

    fun record(loc: Location) {
        coords.add(Pair(loc.latitude, loc.longitude))
    }

    fun getDistance() : Double {
        if (coords.size < 2) return 0.0

        val distances = coords.windowed(2) { window ->
            getDistanceFromLatLon(
                window[0].first,
                window[0].second,
                window[1].first,
                window[1].second
            )
        }

        return distances.sum()
    }

}
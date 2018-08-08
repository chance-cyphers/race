package com.pants.chance.race

fun getDistanceFromLatLon(lat1: Double, lon1: Double, lat2: Double, lon2: Double) : Double {
    val rads = 6371
    val deltaLats = deg2Rad(lat2 - lat1)
    val deltaLons = deg2Rad(lon2 - lon1)
    val a = Math.sin(deltaLats/2) * Math.sin(deltaLats/2) +
            Math.cos(deg2Rad(lat1)) * Math.cos(deg2Rad(lat2)) *
            Math.sin(deltaLons/2) * Math.sin(deltaLons/2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return rads * c
}

private fun deg2Rad(deg: Double) = deg * (Math.PI/180)
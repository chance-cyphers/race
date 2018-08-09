package com.pants.chance.race

import org.junit.Test

import org.junit.Assert.*

class DistanceConverterTest {
    @Test
    fun addition_isCorrect() {
        val distance =
            getDistanceFromLatLon(42.2106744, -83.6209295, 42.2080708, -83.593632)
        val distanceFromSomeCalculatorOnline = 2.267
        assertEquals(distanceFromSomeCalculatorOnline, distance, .001)
    }
}

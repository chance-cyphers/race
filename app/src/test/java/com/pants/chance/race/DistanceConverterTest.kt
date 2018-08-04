package com.pants.chance.race

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DistanceConverterTest {
    @Test
    fun addition_isCorrect() {
        val distance =
            getDistanceFromLatLon(42.2106744F, -83.6209295F, 42.2080708F, -83.593632F)
        val distanceFromSomeCalculatorOnline = 2.267
        assertEquals(distanceFromSomeCalculatorOnline, distance, .001)
    }
}
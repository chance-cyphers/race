package com.pants.chance.race

import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test


class TripTest {

    @Test
    @Ignore
    fun stuff_works() {
//        Trip.record(42.0, -83.0)
//        Trip.record(43.0, -83.0)
//        Trip.record(42.0, -83.0)

        val distance = Trip.getDistance()
        assertEquals(222.4, distance, 0.1)
    }

}


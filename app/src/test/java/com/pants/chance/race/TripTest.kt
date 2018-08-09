package com.pants.chance.race

import android.location.Location
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class TripTest {

    @Mock
    lateinit var loc1: Location
    @Mock
    lateinit var loc2: Location
    @Mock
    lateinit var loc3: Location

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Ignore
    fun stuff_works() {
        `when`(loc1.latitude).thenReturn(42.0)
        `when`(loc1.longitude).thenReturn(-83.0)
        `when`(loc2.latitude).thenReturn(43.0)
        `when`(loc2.longitude).thenReturn(-83.0)
        `when`(loc3.latitude).thenReturn(42.0)
        `when`(loc3.longitude).thenReturn(-83.0)

        Trip.record(loc1)
        Trip.record(loc2)
        Trip.record(loc3)

        val distance = Trip.getDistance()
        val twoDegreesLatitudeInKmSupposedly = 222.4
        assertEquals(twoDegreesLatitudeInKmSupposedly, distance, 0.1)
    }

}


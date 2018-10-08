package com.pants.chance.race

import org.junit.Ignore
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RaceClientTest {

    @Test
    @Ignore
    fun createEntrant_doesSomethingInteresting() {
        raceClient.createEntrant(CreateEntrantRequest("brian boitano"))
            .map { it: Response<CreateEntrantResponse> -> it.body() }
            .subscribe { it ->
                println("entrant: $it")
            }
        Thread.sleep(1500)
    }

    @Test
    fun getTrack_getsFromGivenUrl() {
        raceClient.getTrack("https://race-apu.herokuapp.com/track/17")
            .map { it.body() }
            .subscribe ({ it ->
                println("track: ${it?.status}")
                println("track: ${it?.entrants}")
            }, {
                println("whoops")
            })
        Thread.sleep(1500)
    }
}







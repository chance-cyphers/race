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
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://race-apu.herokuapp.com")
            .build()

        val raceClient = retrofit.create(RaceClient::class.java)

        val entrant = raceClient.createEntrant(CreateEntrantRequest("brian boitano"))
            .map { it: Response<CreateEntrantResponse> -> it.body() }
            .subscribe { it ->
                println("entrant: $it")
            }
    }
}







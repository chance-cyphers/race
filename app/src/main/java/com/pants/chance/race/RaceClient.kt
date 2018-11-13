package com.pants.chance.race

import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface RaceClient {

    @POST("/v2/entrant")
    fun createEntrant(@Body request: CreateEntrantRequest) : Single<Response<CreateEntrantResponse>>

    @GET
    fun getTrack(@Url url: String) : Single<Response<Track>>

    @POST
    fun addLocation(@Url url: String, @Body request: Location) : Single<Response<Location>>

    companion object {
        fun create() : RaceClient {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://race-apu.herokuapp.com")
                .build()

            return retrofit.create(RaceClient::class.java)
        }
    }
}

val raceClient by lazy {
    RaceClient.create()
}

data class CreateEntrantRequest (val userId: String)
data class CreateEntrantResponse (val userId: String, val id: Int, val links: Links) {
    data class Links (val track: String)
}

data class Track (val status: String, val entrants: List<Entrant>, val links: Links) {
    data class Links (val locationUpdate: String)
}

data class Entrant (val id: Int, val userId: String)
data class Location (val time: Long, val lat: Double, val lon: Double)
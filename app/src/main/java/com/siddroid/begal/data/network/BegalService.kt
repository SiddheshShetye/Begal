package com.siddroid.begal.data.network

import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO
import retrofit2.http.GET
import retrofit2.http.Path

internal interface BegalService {
    companion object {
        internal const val THRESHOLD_COUNT = 50
    }

    @GET("breeds/image/random")
    suspend fun getRandomImage(): BegalDTO

    @GET("breeds/image/random/{count}")
    suspend fun getRandomImage(@Path("count") count: Int): BegalListDTO

}
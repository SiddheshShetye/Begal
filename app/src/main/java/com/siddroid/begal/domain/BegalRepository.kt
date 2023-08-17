package com.siddroid.begal.domain

import com.siddroid.begal.core.network.Resource
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO

internal interface BegalRepository {
    suspend fun getImageFromNetwork(): Resource<BegalDTO>
    suspend fun getImagesFromNetwork(count: Int): Resource<BegalListDTO>
    suspend fun getNextImageFromLocal(): Resource<BegalDTO>
    suspend fun getPreviousImageFromLocal(): Resource<BegalDTO>
    suspend fun addImageToLocal(begalDTO: BegalDTO, maxEntries: Int)
    fun getCurrentIndex(): Int
}
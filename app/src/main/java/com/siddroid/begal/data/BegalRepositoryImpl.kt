package com.siddroid.begal.data

import com.siddroid.begal.core.BegalKoinComponent
import com.siddroid.begal.core.network.NetworkStatusHelper
import com.siddroid.begal.core.network.Resource
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO
import com.siddroid.begal.data.network.BegalService
import com.siddroid.begal.domain.BegalRepository

internal class BegalRepositoryImpl(val begalService: BegalService, val networkStatusHelper: NetworkStatusHelper, val localDataStore: LocalDataStore): BegalRepository, BegalKoinComponent {
    override suspend fun getImageFromNetwork(): Resource<BegalDTO> {
        if (!networkStatusHelper.isConnected()) {
            return Resource.noInternet
        }
        val response = begalService.getRandomImage()
        return if (response.message.isNullOrBlank() || response.status.isNullOrBlank()){
            Resource.unKnownError
        } else if (response.status == "success"){
            Resource.success(response)
        } else {
            Resource.error(null, response.message.ifEmpty { "Something went wrong" })
        }
    }

    override suspend fun getImagesFromNetwork(count: Int): Resource<BegalListDTO> {
        if (!networkStatusHelper.isConnected()) {
            return Resource.noInternet
        }
        val response = begalService.getRandomImage(if (count > BegalService.THRESHOLD_COUNT) BegalService.THRESHOLD_COUNT else count )
        return if (response.message.isNullOrEmpty() || response.status.isNullOrBlank()){
            Resource.unKnownError
        } else if (response.status == "success"){
            Resource.success(response)
        } else {
            Resource.error(null, "Something went wrong")
        }
    }

    override suspend fun getNextImage(): Resource<BegalDTO> {
        val response = localDataStore.getNext()
        return if (response.status == "success") Resource.success(response) else Resource.error(response, response.message.orEmpty())
    }

    override suspend fun getPreviousImageFromLocal(): Resource<BegalDTO> {
        val response = localDataStore.getPrevious()
        return Resource.success(response)
    }

    override suspend fun addImageToLocal(begalDTO: BegalDTO, maxEntries: Int) {
        localDataStore.addToLocalData(begalDTO, maxEntries)
    }

    override fun getCurrentIndex(): Int {
        return localDataStore.currentIndex
    }
}
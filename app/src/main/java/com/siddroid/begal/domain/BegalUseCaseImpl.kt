package com.siddroid.begal.domain

import android.util.Log
import com.siddroid.begal.core.BegalConfig
import com.siddroid.begal.core.network.Resource
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO

class BegalUseCaseImpl(val repository: BegalRepository): BegalUseCase {
    override suspend fun invoke(config: BegalConfig): Resource<BegalDTO> {
        val response = repository.getImageFromNetwork()
        Log.d("CHECK", "response in invoke $response")
        if (response.status == Resource.Status.SUCCESS && config.enableMemCache) {
            response.data?.let { repository.addImageToLocal(it, config.memCacheConfig.maxEntries) }
        }
        return response
    }

    override suspend fun invoke(count: Int, config: BegalConfig): Resource<BegalListDTO> {
        val response = repository.getImagesFromNetwork(count)
        if (response.status == Resource.Status.SUCCESS && config.enableMemCache) {
            response.data?.let { it.message?.forEach { msg -> repository.addImageToLocal(BegalDTO(msg, it.status), config.memCacheConfig.maxEntries)} }
        }
        return response
    }

    override suspend fun invokePrevious(config: BegalConfig): Resource<BegalDTO> {
        return if (config.enableMemCache) {
            repository.getPreviousImageFromLocal()
        } else {
            Resource.error(null, "Memory caching must be enabled to get previous images")
        }
    }

    override suspend fun invokeNext(config: BegalConfig): Resource<BegalDTO> {
        return if (config.enableMemCache) {
            repository.getNextImageFromLocal()
        } else {
            Resource.error(null, "You have reached at latest object.")
        }
    }

    override fun getCurrentIndex(): Int {
        return repository.getCurrentIndex()
    }
}
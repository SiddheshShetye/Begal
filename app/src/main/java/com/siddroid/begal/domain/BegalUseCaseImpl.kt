package com.siddroid.begal.domain

import android.util.Log
import com.siddroid.begal.core.BegalConfig
import com.siddroid.begal.core.network.Resource
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO
import com.siddroid.begal.domain.model.BegalEntityData
import com.siddroid.begal.domain.model.Data
import com.siddroid.begal.domain.model.EntityState

class BegalUseCaseImpl(val repository: BegalRepository): BegalUseCase {
    override suspend fun invoke(config: BegalConfig): BegalEntityData<List<Data>> {
        val response = repository.getImageFromNetwork()
        val entity = response.toBegalEntity()
        Log.d("CHECK", "response in invoke $response")
        if (response.status == Resource.Status.SUCCESS && config.enableMemCache) {
            response.data?.let { repository.addImageToLocal(it, config.memCacheConfig.maxEntries) }
        }
        entity.index = getCurrentIndex()
        return entity
    }

    override suspend fun invoke(count: Int, config: BegalConfig): BegalEntityData<List<Data>> {
        val response = repository.getImagesFromNetwork(count)
        if (response.status == Resource.Status.SUCCESS && config.enableMemCache) {
            response.data?.let { it.message?.forEach { msg -> repository.addImageToLocal(BegalDTO(msg, it.status), config.memCacheConfig.maxEntries)} }
        }
        return response.toBegalListEntity().also { it.index = getCurrentIndex() }
    }

    override suspend fun invokePrevious(config: BegalConfig): BegalEntityData<List<Data>> {
        return if (config.enableMemCache) {
            repository.getPreviousImageFromLocal().toBegalEntity().also { it.index = getCurrentIndex() }
        } else {
            Resource.error(null as BegalDTO?, "Memory caching must be enabled to get previous images").toBegalEntity()
        }
    }

    override suspend fun invokeNext(config: BegalConfig): BegalEntityData<List<Data>> {
        return if (config.enableMemCache) {
            repository.getNextImageFromLocal().toBegalEntity().also { it.index = getCurrentIndex() }
        } else {
            Resource.error(null as BegalDTO?, "You have reached at latest object.").toBegalEntity()
        }
    }

    override fun getCurrentIndex(): Int {
        return repository.getCurrentIndex()
    }

    private fun Resource<BegalDTO>.toBegalEntity(): BegalEntityData<List<Data>> {
        return BegalEntityData(
            state = if (status == Resource.Status.ERROR || status == Resource.Status.UNKNOWN || status == Resource.Status.NO_INTERNET) EntityState.ERROR else EntityState.SUCCESS,
            message = message.orEmpty(),
            data = listOf(Data(data?.message.orEmpty()))
        )
    }

    private fun Resource<BegalListDTO>.toBegalListEntity(): BegalEntityData<List<Data>> {
        return BegalEntityData(
            state = if (status == Resource.Status.ERROR || status == Resource.Status.UNKNOWN || status == Resource.Status.NO_INTERNET) EntityState.ERROR else EntityState.SUCCESS,
            message = message.orEmpty(),
            data = data?.getDataList()
        )
    }
}

private fun BegalListDTO.getDataList():List<Data> {
    val list = mutableListOf<Data>()
    message?.forEach {
        list.add(Data(it))
    }
    return list
}
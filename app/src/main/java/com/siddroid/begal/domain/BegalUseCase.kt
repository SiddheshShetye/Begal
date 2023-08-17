package com.siddroid.begal.domain

import com.siddroid.begal.core.BegalConfig
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO
import com.siddroid.begal.domain.model.BegalEntityData
import com.siddroid.begal.domain.model.Data

internal interface BegalUseCase {

    suspend fun invoke(config: BegalConfig): BegalEntityData<List<Data>>
    suspend fun invoke(count: Int, config: BegalConfig): BegalEntityData<List<Data>>
    suspend fun invokePrevious(config: BegalConfig): BegalEntityData<List<Data>>
    suspend fun invokeNext(config: BegalConfig): BegalEntityData<List<Data>>
    fun getCurrentIndex(): Int
}
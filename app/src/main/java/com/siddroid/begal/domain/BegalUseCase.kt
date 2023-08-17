package com.siddroid.begal.domain

import com.siddroid.begal.core.BegalConfig
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO
import com.siddroid.begal.domain.model.BegalEntityData

interface BegalUseCase {

    suspend fun invoke(config: BegalConfig): BegalEntityData<BegalDTO>
    suspend fun invoke(count: Int, config: BegalConfig): BegalEntityData<BegalListDTO>
    suspend fun invokePrevious(config: BegalConfig): BegalEntityData<BegalDTO>
    suspend fun invokeNext(config: BegalConfig): BegalEntityData<BegalDTO>
    fun getCurrentIndex(): Int
}
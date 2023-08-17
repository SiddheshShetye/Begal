package com.siddroid.begal.domain

import com.siddroid.begal.core.BegalConfig
import com.siddroid.begal.core.network.Resource
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.data.model.BegalListDTO

interface BegalUseCase {

    suspend fun invoke(config: BegalConfig): Resource<BegalDTO>
    suspend fun invoke(count: Int, config: BegalConfig): Resource<BegalListDTO>
    suspend fun invokePrevious(config: BegalConfig): Resource<BegalDTO>
    suspend fun invokeNext(config: BegalConfig): Resource<BegalDTO>
    fun getCurrentIndex(): Int
}
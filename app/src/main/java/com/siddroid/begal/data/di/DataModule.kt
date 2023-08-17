package com.siddroid.begal.data.di

import com.siddroid.begal.data.BegalRepositoryImpl
import com.siddroid.begal.data.LocalDataStore
import com.siddroid.begal.data.network.BegalService
import com.siddroid.begal.domain.BegalRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {
    factory { get<Retrofit>().create(BegalService::class.java)}
    single { LocalDataStore() }
    factoryOf(::BegalRepositoryImpl) { bind<BegalRepository>()}
}
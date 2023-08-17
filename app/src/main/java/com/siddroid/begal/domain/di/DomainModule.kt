package com.siddroid.begal.domain.di

import com.siddroid.begal.domain.BegalUseCase
import com.siddroid.begal.domain.BegalUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::BegalUseCaseImpl) { bind<BegalUseCase>()}
}
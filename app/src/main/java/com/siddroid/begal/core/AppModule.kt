package com.siddroid.begal.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("DEFAULT")) { Dispatchers.Default }
    single(named("IO")) { Dispatchers.IO }
    single<CoroutineDispatcher>(named("MAIN")) { Dispatchers.Main }
}
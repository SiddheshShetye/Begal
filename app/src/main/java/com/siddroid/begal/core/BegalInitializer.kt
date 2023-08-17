package com.siddroid.begal.core

import android.content.Context
import androidx.startup.Initializer
import com.siddroid.begal.core.network.networkModule
import com.siddroid.begal.data.di.dataModule
import com.siddroid.begal.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication

internal class BegalInitializer : Initializer<Koin> {

    override fun create(context: Context): Koin {
        return koinApplication {
            androidContext(context)
            androidLogger(Level.ERROR)
            modules(listOf(appModule, networkModule, dataModule, domainModule))
        }.koin
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
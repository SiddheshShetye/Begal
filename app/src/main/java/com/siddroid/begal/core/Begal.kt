package com.siddroid.begal.core

import android.app.Application
import android.util.Log
import androidx.startup.AppInitializer
import com.siddroid.begal.domain.BegalUseCase
import com.siddroid.begal.domain.model.BegalEntityData
import com.siddroid.begal.domain.model.Data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named


object Begal : BegalKoinComponent {

    private lateinit var config: BegalConfig
    private val begalUseCase: BegalUseCase by inject()
    private val mainDispatcher: CoroutineDispatcher by inject(named("MAIN"))
    private val ioDispatcher: CoroutineDispatcher by inject(named("IO"))

    fun getImage(callback: (BegalEntityData<List<Data>>) -> Unit) {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        CoroutineScope(ioDispatcher).launch {
            callback(BegalEntityData.loading())
            val response = begalUseCase.invoke(config)
            Log.d("CHECK", " DTO in Begal = $response")
            withContext(mainDispatcher) {
                callback(response)
            }
        }
    }

    fun getImages(count: Int, callback: (BegalEntityData<List<Data>>) -> Unit) {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        CoroutineScope(ioDispatcher).launch {
            callback(BegalEntityData.loading())
            val response = begalUseCase.invoke(count, config)
            withContext(mainDispatcher) {
                callback(response)
            }
        }
    }

    fun getNexImage(callback: (BegalEntityData<List<Data>>) -> Unit) {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        CoroutineScope(ioDispatcher).launch {
            callback(BegalEntityData.loading())
            val response = begalUseCase.invokeNext(config)
            withContext(mainDispatcher) {
                callback(response)
            }
        }
    }

    fun getPreviousImage(callback: (BegalEntityData<List<Data>>) -> Unit) {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        CoroutineScope(ioDispatcher).launch {
            callback(BegalEntityData.loading())
            val response = begalUseCase.invokePrevious(config)
            withContext(mainDispatcher) {
                callback(response)
            }
        }
    }

    internal fun saveConfig(config: BegalConfig) {
        this.config = config
    }

    private fun isInitialized(): Boolean {
        return this::config.isInitialized
    }

    fun getCurrentIndex(): Int {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        return begalUseCase.getCurrentIndex()
    }
}

object BegalInit {

    internal lateinit var koinRef: Koin

    fun init(application: Application, config: BegalConfig) {
        koinRef = AppInitializer.getInstance(application)
            .initializeComponent(BegalInitializer::class.java)
        Begal.saveConfig(config)
    }
}

interface BegalKoinComponent : KoinComponent {
    override fun getKoin(): Koin {
        return BegalInit.koinRef
    }
}




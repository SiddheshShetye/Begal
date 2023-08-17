package com.siddroid.begal.core

import android.app.Application
import android.util.Log
import androidx.startup.AppInitializer
import com.siddroid.begal.data.model.BegalDTO
import com.siddroid.begal.domain.BegalUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine


object Begal: BegalKoinComponent {

    private lateinit var config: BegalConfig
    private val begalUseCase: BegalUseCase by inject()

//    suspend fun getImage(): BegalDTO = suspendCancellableCoroutine<BegalDTO> {
//        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
//        val response = begalUseCase.invoke(config)
//        it.resume(response.data!!) { throw Exception() }
//    }

    fun getImage(callback: (BegalDTO) -> Unit)  {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        CoroutineScope(Dispatchers.IO).launch {
            val response = begalUseCase.invoke(config)
            val dto = response.data
            Log.d("CHECK", " DTO in Begal = $dto")
            withContext(Dispatchers.Main) {
                dto?.let { callback(it) }
            }
        }

//        it.resume(response.data!!) { throw Exception() }
    }

    suspend fun getImages(count: Int) {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        begalUseCase.invoke(count, config)
    }

    suspend fun getNexImage() {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        begalUseCase.invokeNext(config)
    }

    suspend fun getPreviousImage() {
        if (!isInitialized()) throw IllegalStateException("Please Initialize using BegalInit.init()")
        begalUseCase.invokePrevious(config)
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




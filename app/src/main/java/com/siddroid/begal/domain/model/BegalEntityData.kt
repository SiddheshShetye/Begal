package com.siddroid.begal.domain.model

import androidx.annotation.Keep

@Keep
data class BegalEntityData<out T>(val state: EntityState, val message: String, var index: Int = -1, val data: T?) {
    companion object {
        fun loading() = BegalEntityData(EntityState.LOADING, "", -1, null)
    }
}

@Keep
data class Data(val url: String)

@Keep
enum class EntityState {
    SUCCESS,
    ERROR,
    LOADING
}
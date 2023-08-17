package com.siddroid.begal.domain.model

data class BegalEntityData<out T>(val state: EntityState, val message: String, var index: Int = -1, val data: T?) {
    companion object {
        fun loading() = BegalEntityData(EntityState.LOADING, "", -1, null)
    }
}

data class Data(val url: String)

enum class EntityState {
    SUCCESS,
    ERROR,
    LOADING
}
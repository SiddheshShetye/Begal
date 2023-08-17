package com.siddroid.begal.core.network

data class Resource<out T>(val status: Status,
                           val data: T? = null,
                           val message: String? = "") {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null)

        val unKnownError: Resource<Nothing> = Resource(status = Status.UNKNOWN)
        val noInternet: Resource<Nothing> = Resource(status = Status.NO_INTERNET)
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        UNKNOWN,
        NO_INTERNET
    }
}


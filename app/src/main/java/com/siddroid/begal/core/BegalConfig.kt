package com.siddroid.begal.core

data class BegalConfig(val enableMemCache: Boolean, val memCacheConfig: MemCacheConfig = MemCacheConfig())

data class MemCacheConfig(val maxEntries: Int = 200)

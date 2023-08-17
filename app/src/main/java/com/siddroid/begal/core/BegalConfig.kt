package com.siddroid.begal.core

data class BegalConfig(val enableMemCache: Boolean, val memCacheConfig: MemCacheConfig)

data class MemCacheConfig(val maxEntries: Int)

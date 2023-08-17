package com.siddroid.begal.core

import android.app.Application
import android.content.Context
import com.siddroid.begal.core.network.networkModule
import com.siddroid.begal.data.di.dataModule
import com.siddroid.begal.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.dsl.koinApplication


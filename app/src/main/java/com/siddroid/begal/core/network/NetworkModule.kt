package com.siddroid.begal.core.network


import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single(named("base")) { "https://dog.ceo/api/" }
    single { provideConnectivityManager(androidContext()) }
    single { provideNetworkStatusHelper(get()) }
    single { provideHttpLogging() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get(), get(named("base"))) }
}

private fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit? {
    val gson = GsonBuilder()
        .setLenient()
        .create()
    return Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideHttpLogging(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
) = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .writeTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(
        30,
        TimeUnit.SECONDS
    ).build()

fun provideNetworkStatusHelper(connectivityManager: ConnectivityManager): NetworkStatusHelper {
    return NetworkStatusHelper(connectivityManager)
}

fun provideConnectivityManager(context: Context): ConnectivityManager {
    return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}
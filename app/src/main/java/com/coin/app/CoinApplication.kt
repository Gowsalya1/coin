package com.coin.app

import android.app.Application
import com.coin.BuildConfig
import com.coin.api.ApiConstant
import com.coin.api.ApiPath
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class CoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: CoinApplication? = null


        private val INTERCEPTOR = Interceptor { chain ->
            val build = chain.request().newBuilder()
                .addHeader(ApiConstant.CONTENT_TYPE, ApiConstant.CONTENT_TYPE_JSON)
                .addHeader(ApiConstant.X_CMC_PRO_API_KEY, ApiConstant.API_KEY)
            chain.proceed(build.build())
        }
        val RETROFIT: Retrofit = Retrofit.Builder()
            .baseUrl(ApiPath.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        private val client: OkHttpClient
            get() {
                val client = OkHttpClient.Builder()
                    .connectTimeout(ApiConstant.API_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                    .readTimeout(ApiConstant.API_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                    .writeTimeout(ApiConstant.API_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                    .addInterceptor(INTERCEPTOR)
                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    client.addInterceptor(interceptor)
                }
                return client.build()
            }
    }
}
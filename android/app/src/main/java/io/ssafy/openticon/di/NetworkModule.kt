package io.ssafy.openticon.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ssafy.openticon.MainActivity
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import io.ssafy.openticon.data.remote.MemberApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.Request
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://apitest.openticon.store/api/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val token = runBlocking { TokenDataSource.token.firstOrNull() }
        Log.d("use token", "Access Token: $token")
        return OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val originalRequest: Request = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideEmoticonPacksApi(retrofit: Retrofit): EmoticonPacksApi {
        return retrofit.create(EmoticonPacksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberApi(retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return BASE_URL
    }

}
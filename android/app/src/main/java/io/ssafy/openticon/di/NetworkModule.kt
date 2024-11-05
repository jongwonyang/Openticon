package io.ssafy.openticon.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ssafy.openticon.data.local.TokenDataSource
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import io.ssafy.openticon.data.remote.MemberApi
import io.ssafy.openticon.data.remote.PointsApi
import io.ssafy.openticon.data.remote.PurchaseApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://apitest.openticon.store"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val originalRequest: Request = chain.request()
                val token = runBlocking { TokenDataSource.token.firstOrNull() }
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)

            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
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

    @Provides
    @Singleton
    fun providePointsApi(retrofit: Retrofit): PointsApi {
        return retrofit.create(PointsApi::class.java)
    }

    @Provides
    @Singleton
    fun providePurchaseApi(retrofit: Retrofit): PurchaseApi {
        return retrofit.create(PurchaseApi::class.java)
    }
}
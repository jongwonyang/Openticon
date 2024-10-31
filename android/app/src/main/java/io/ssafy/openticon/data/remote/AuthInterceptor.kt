package io.ssafy.openticon.data.remote

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.ssafy.openticon.data.local.TokenDataSource
import kotlinx.coroutines.flow.firstOrNull
import android.util.Log
import io.ssafy.openticon.data.remote.NetworkConfig;

class AuthInterceptor(
    private val tokenDataSource: TokenDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")

        val token = runBlocking { tokenDataSource.token.firstOrNull() }
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            Log.d("use token", "Access Token: $token")
        }
        Log.d("AuthInterceptor", "Request URL: ${chain.request().url}")
        return chain.proceed(requestBuilder.build())
    }
}

//fun createApiClient(context: Context): MemberApi {
//    val tokenDataSource = TokenDataSource(context)
//    val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor(tokenDataSource))
//        .build()
//
//    return Retrofit.Builder()
//        .baseUrl(NetworkConfig.BaseURL)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(MemberApi::class.java)
//}


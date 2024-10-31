package io.ssafy.openticon.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.ssafy.openticon.data.local.TokenDataSource

object NetworkConfig {
    const val BaseURL = "https://apitest.openticon.store/api/v1/"
//    const val BaseURL = "http://10.0.2.2:8080/api/v1/"
}

class ApiClient() {
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(TokenDataSource)) // TokenDataSource에 context 전달
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BaseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 각 API 인터페이스에 대한 인스턴스를 가져옵니다.
    val memberApi: MemberApi by lazy {
        retrofit.create(MemberApi::class.java)
    }

//    val anotherApi: AnotherApi by lazy {
//        retrofit.create(AnotherApi::class.java)
//    }
}

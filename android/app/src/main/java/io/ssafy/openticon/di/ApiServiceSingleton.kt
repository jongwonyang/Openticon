//package io.ssafy.openticon.di
//
//import io.ssafy.openticon.data.remote.ApiClient
//import io.ssafy.openticon.data.remote.MemberApiService
//
//object ApiServiceSingleton {
//    val instance: MemberApiService by lazy {
//        val apiClient = ApiClient()  // ApiClient 초기화
//        MemberApiService(apiClient.memberApi)
//    }
//}
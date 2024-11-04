package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.data.remote.MemberApi
import retrofit2.Response
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val api: MemberApi
) {
    suspend fun getMemberInfo() : Response<MemberEntity?> {
        return api.getMemberInfo()
    }
}
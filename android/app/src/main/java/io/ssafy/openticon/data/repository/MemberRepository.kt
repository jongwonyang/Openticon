    package io.ssafy.openticon.data.repository

    import io.ssafy.openticon.data.model.MemberEntity
    import io.ssafy.openticon.data.remote.MemberApi
    import okhttp3.MultipartBody
    import retrofit2.Response
    import retrofit2.http.Part
    import javax.inject.Inject

    class MemberRepository @Inject constructor(
        private val api: MemberApi
    ) {
        suspend fun getMemberInfo() : Response<MemberEntity?> {
            return api.getMemberInfo()
        }
        suspend fun editProfile(nickname: String, profileImage: MultipartBody.Part?): Response<String> {
            return api.editProfile(nickname, profileImage)
        }

        suspend fun deleteMember() : Response<Unit> {
            return api.deleteMember()
        }


    }
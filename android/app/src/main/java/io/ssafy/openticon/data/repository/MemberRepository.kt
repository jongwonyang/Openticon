        package io.ssafy.openticon.data.repository

        import io.ssafy.openticon.data.model.MemberEntity
        import io.ssafy.openticon.data.remote.MemberApi
        import okhttp3.MultipartBody
        import okhttp3.RequestBody.Companion.toRequestBody
        import retrofit2.Response
        import retrofit2.http.Part
        import javax.inject.Inject

        class MemberRepository @Inject constructor(
            private val api: MemberApi
        ) {
            suspend fun getMemberInfo() : Response<MemberEntity?> {
                return api.getMemberInfo()
            }
            suspend fun editProfile(nickname: String, bio : String, profileImage: MultipartBody.Part?): Response<String> {
                return api.editProfile(nickname.toRequestBody(), bio.toRequestBody(), profileImage)
            }
            suspend fun deleteMember() : Response<Unit> {
                return api.deleteMember()
            }
            suspend fun duplicateCheck(nickname: String) : Response<Boolean> {
                return api.duplicateCheck(nickname)
            }
            suspend fun getWriterInfo(nickname: String): Response<MemberEntity?> {
                return api.getWriterInfo(nickname)
            }
        }
package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.data.repository.MemberRepository
import io.ssafy.openticon.domain.model.ResponseWithStatus
import io.ssafy.openticon.ui.viewmodel.MemberViewModel
import retrofit2.Response
import java.lang.reflect.Member
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): Result<ResponseWithStatus<MemberEntity?>> {
        return try {
            // Repository에서 데이터 가져오기
            val memberData: Response<MemberEntity?> = repository.getMemberInfo()
            // 상태 코드 및 데이터를 기반으로 ResponseWithStatus 생성
            val response = ResponseWithStatus(
                data = memberData.body(),          // Response의 실제 데이터를 할당
                status = memberData.code()         // 실제 API 응답 상태 코드 사용
            )
            // 성공 결과 반환
            Result.success(response)
        } catch (e: Exception) {
            // 실패 결과 반환
            Result.failure(e)
        }
    }
}
//    suspend operator fun invoke(
//        searchKey: String,
//        searchText: String,
//        sort: String = "new",
//        size: Int = 10,
//        page: Int = 0
//    ): Pair<List<SearchEmoticonPacksListItem>, Boolean> {
//
//        return repository.searchEmoticonPacks(
//            type = searchKey,
//            query = searchText,
//            sort = sort,
//            size = size,
//            page = page
//        ).toSearchEmoticonPacksList()
//    }
//}
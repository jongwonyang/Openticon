package io.ssafy.openticon.ui.sample

import io.ssafy.openticon.domain.model.Author

object AuthorSampleData {
    val sampleAuthor = Author(
        id = 1,
        name = "무치",
        profileImage = "https://picsum.photos/200/200?r=100",
        createdAt = "2023-08-10"
    )
}
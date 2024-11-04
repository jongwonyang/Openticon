package io.ssafy.openticon.domain.model

data class PurchaseInfo (
    val packId: Int,
    val purchased: Boolean,
    val downloaded: Boolean
)
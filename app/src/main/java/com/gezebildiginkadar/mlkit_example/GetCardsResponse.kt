package com.gezebildiginkadar.mlkit_example

data class CardResponse(
    val card_id: String,
    val state: String,
    val created_at: String,
    val details: Details
)

data class Details(
    val group_number: DetailItem,
    val member_number: DetailItem,
    val rx_bin: DetailItem,
    val rx_pcn: DetailItem,
    val member_name: DetailItem,
    val plan_name: DetailItem,
    val plan_id: DetailItem,
    val card_specific_id: DetailItem
)

data class DetailItem(
    val value: String,
    val scores: List<Double>
)

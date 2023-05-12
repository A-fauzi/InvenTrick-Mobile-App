package com.example.warehouseproject.domain.modelentities.product

data class StockHistory(
    val _id: String,
    val code_items: String,
    val name: String,
    val qty: String,
    val status: String,
    val user_id: String? = null,
    val created_at: String,
) {
    data class StockHistoryRequest(
        val code_items: String,
        val name: String,
        val qty: String,
        val status: String,
        val user_id: String,
    )
    data class StockHistorySingleResponse(
        val message: String,
//        val count: String,
        val data: StockHistory
    )
    data class StockHistoryAllResponse(
        val message: String,
        val count: String,
        val data: List<StockHistory>
    )
}

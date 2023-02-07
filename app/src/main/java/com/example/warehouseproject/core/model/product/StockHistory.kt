package com.example.warehouseproject.core.model.product

data class StockHistory(
    val code_items: String,
    val name: String,
    val qty: String,
    val status: String,
    val created_at: String,
) {
    data class StockHistoryRequest(
        val code_items: String,
        val name: String,
        val qty: String,
        val status: String,
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

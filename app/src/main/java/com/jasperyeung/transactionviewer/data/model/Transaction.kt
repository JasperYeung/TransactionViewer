package com.jasperyeung.transactionviewer.data.model

data class Transaction(
    val transaction_id: String,
    val stock_symbol: String,
    val buy_sell: String,
    val quantity: Int,
    val price: Double,
    val timestamp: String,
    val order_type: String
)
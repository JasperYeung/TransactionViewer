package com.jasperyeung.transactionviewer.data.model

enum class BuySellType(val value: String) {
    BUY("buy"),
    SELL("sell");

    companion object {
        fun fromValue(value: String?): BuySellType? {
            if (value == null) return null
            return entries.find { it.value == value }
        }
    }
}
package com.jasperyeung.transactionviewer.data.model

enum class OrderType(val value: String) {
    MARKET_ORDER("M"),
    LIMIT_ORDER("LO");

    companion object {
        fun fromValue(value: String?): OrderType? {
            if (value == null) return null
            return entries.find { it.value == value }
        }
    }
}
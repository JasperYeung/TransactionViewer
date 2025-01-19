package com.jasperyeung.transactionviewer.data.repository

import com.jasperyeung.transactionviewer.BuildConfig
import com.jasperyeung.transactionviewer.data.api.TransactionApiService
import com.jasperyeung.transactionviewer.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TransactionRepository(private val transactionApiService: TransactionApiService) {

    fun fetchTransactionHistory(): Flow<List<Transaction>> = flow {
        val transactionHistoryList =
            transactionApiService.getTransactionHistory(BuildConfig.API_KEY)
        emit(transactionHistoryList)
    }.map { transactionHistoryList ->
        transactionHistoryList.sortedByDescending { it.timestamp } //sort by timestamp in descending order
    }
}
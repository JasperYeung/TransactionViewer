package com.jasperyeung.transactionviewer.data.api

import com.jasperyeung.transactionviewer.data.model.Transaction
import retrofit2.http.GET
import retrofit2.http.Header

interface TransactionApiService {

    @GET("v1/stock/txn")
    suspend fun getTransactionHistory(
        @Header("x-api-key") apiKey: String
    ): List<Transaction>

}
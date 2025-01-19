package com.jasperyeung.transactionviewer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasperyeung.transactionviewer.data.api.NetworkState
import com.jasperyeung.transactionviewer.data.model.Transaction
import com.jasperyeung.transactionviewer.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _transactionHistoryList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionHistoryList = _transactionHistoryList.asStateFlow()

    private val _networkState = MutableStateFlow(NetworkState.INITIAL)
    val networkState = _networkState.asStateFlow()

    fun fetchTransactionHistory() {
        viewModelScope.launch {
            _networkState.value = NetworkState.LOADING
            try {
                repository.fetchTransactionHistory().collect { transactions ->
                    _transactionHistoryList.value = transactions
                    _networkState.value = NetworkState.SUCCESS
                    Log.d("TransactionViewModel", "Transaction history fetched successfully")
                }
            } catch (e: Exception) {
                _networkState.value = NetworkState.ERROR
                Log.e("TransactionViewModel", "Error fetching transaction history", e)
            }
        }
    }
}
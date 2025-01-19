package com.jasperyeung.transactionviewer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.jasperyeung.transactionviewer.data.api.RetrofitClient
import com.jasperyeung.transactionviewer.data.repository.TransactionRepository
import com.jasperyeung.transactionviewer.viewmodel.TransactionViewModel
import com.jasperyeung.transactionviewer.viewmodel.TransactionViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(TransactionRepository(RetrofitClient.transactionApiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TransactionHistoryComposable(viewModel)
        }
    }
}
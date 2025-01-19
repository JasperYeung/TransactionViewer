package com.jasperyeung.transactionviewer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jasperyeung.transactionviewer.R
import com.jasperyeung.transactionviewer.data.api.NetworkState
import com.jasperyeung.transactionviewer.data.model.BuySellType
import com.jasperyeung.transactionviewer.data.model.OrderType
import com.jasperyeung.transactionviewer.data.model.Transaction
import com.jasperyeung.transactionviewer.ui.theme.PrimaryBlue
import com.jasperyeung.transactionviewer.ui.theme.SecondaryOrange
import com.jasperyeung.transactionviewer.ui.theme.StockGreen
import com.jasperyeung.transactionviewer.ui.theme.StockRed
import com.jasperyeung.transactionviewer.ui.theme.DividerGrey
import com.jasperyeung.transactionviewer.ui.theme.TitleGrey
import com.jasperyeung.transactionviewer.utils.DateUtils
import com.jasperyeung.transactionviewer.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryComposable(viewModel: TransactionViewModel) {
    val transactionHistoryList by viewModel.transactionHistoryList.collectAsState()
    val networkState by viewModel.networkState.collectAsState()

    val isRefreshing = networkState == NetworkState.LOADING
    val pullRefreshState = rememberPullToRefreshState()

    val onRefresh: () -> Unit = {
        viewModel.fetchTransactionHistory()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchTransactionHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.transaction_history)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                )
            )
        }
    ) { innerPadding ->

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                //.padding(innerPadding)
            ) {

                //center loading icon, now replaced with pull to refresh
                val (loadingIndicator, content) = createRefs()

                /*if (networkState == NetworkState.LOADING) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .constrainAs(loadingIndicator) {
                                centerTo(parent)
                            },
                        color = SecondaryOrange
                    )
                } else {*/
                Column(
                    modifier = Modifier.constrainAs(content) {
                        top.linkTo(parent.top)
                    }
                ) {
                    if (networkState == NetworkState.SUCCESS) {
                        HeaderBar()
                        TransactionList(transactionHistoryList)

                    } else if (networkState == NetworkState.ERROR) {
                        Text(
                            stringResource(R.string.error_try_again_later),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            color = TitleGrey,
                            fontSize = 16.sp

                        )
                    }

                }
                //}
            }
        }

    }
}

@Composable
fun HeaderBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text( //order type
            text = stringResource(R.string.title_order_type),
            modifier = Modifier.width(90.dp),
            color = TitleGrey
        )

        Text( //stock symbol
            text = stringResource(R.string.title_stock_symbol),
            modifier = Modifier.width(70.dp),
            color = TitleGrey
        )

        Text( // quantity/price
            text = stringResource(R.string.title_quantity_and_price),
            modifier = Modifier.width(90.dp),
            textAlign = TextAlign.End,
            color = TitleGrey
        )

        Text( // transaction time
            text = stringResource(R.string.title_transaction_time),
            modifier = Modifier.width(120.dp),
            textAlign = TextAlign.End,
            color = TitleGrey
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(top = 4.dp),
        thickness = 1.dp,
        color = DividerGrey
    )
}

@Composable
fun TransactionList(transactionList: List<Transaction>) {
    var currentPage by remember { mutableStateOf(1) }
    val pageSize = 5
    val paginatedTransactionList = transactionList.take(currentPage * pageSize)

    val transactionListState = rememberLazyListState()

    LazyColumn(
        state = transactionListState
    ) {
        itemsIndexed(paginatedTransactionList) { _, transaction ->
            TransactionItem(transaction)
        }
        item {
            if (paginatedTransactionList.size < transactionList.size) {
                Button(
                    onClick = { currentPage++ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryOrange
                    )
                ) {
                    Text(stringResource(R.string.load_more))
                }

            } else if (transactionList.isNotEmpty()) { //end of list
                Text(
                    text = stringResource(R.string.all_records_loaded),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    color = TitleGrey

                )

            } else if (transactionList.isEmpty()) { //no records
                Text(
                    text = stringResource(R.string.no_records),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = TitleGrey

                )
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val buySellType = BuySellType.fromValue(transaction.buy_sell)
    val orderType = OrderType.fromValue(transaction.order_type)
    val (formattedDate, formattedTime) = DateUtils.formatTimestamp(transaction.timestamp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(90.dp)
            ) {
                Text( //buy sell type
                    text = when (buySellType) {
                        BuySellType.BUY -> stringResource(R.string.buy)
                        BuySellType.SELL -> stringResource(R.string.sell)
                        null -> stringResource(R.string.unknown)
                    },
                    color = when (buySellType) {
                        BuySellType.BUY -> StockGreen
                        BuySellType.SELL -> StockRed
                        null -> Color.Gray
                    },
                    fontSize = 18.sp
                )

                Text( //order type
                    text = when (orderType) {
                        OrderType.LIMIT_ORDER -> stringResource(R.string.limit_order)
                        OrderType.MARKET_ORDER -> stringResource(R.string.market_order)
                        null -> stringResource(R.string.unknown)
                    }
                )
            }

            Text( //stock symbol
                text = transaction.stock_symbol
                    ?: stringResource(R.string.unknown), //in case API returns null
                fontSize = 18.sp,
                modifier = Modifier.width(70.dp)
            )

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(90.dp)
            ) {
                Text( //quantity
                    text = transaction.quantity.toString(),
                    fontSize = 18.sp
                )

                Text( //price
                    text = transaction.price.toString()
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(120.dp)
            ) {
                Text( //date
                    text = formattedDate
                        ?: stringResource(R.string.unknown),  //in case API returns null
                    fontSize = 18.sp
                )

                Text( //time
                    text = formattedTime
                        ?: stringResource(R.string.unknown) //in case API returns null
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = DividerGrey
        )
    }
}
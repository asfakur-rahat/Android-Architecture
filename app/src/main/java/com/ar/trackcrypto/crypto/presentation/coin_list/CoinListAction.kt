package com.ar.trackcrypto.crypto.presentation.coin_list

import com.ar.trackcrypto.crypto.presentation.models.CoinUi

sealed interface CoinListAction {
    data class OnCoinClick(val coinUi: CoinUi): CoinListAction
}
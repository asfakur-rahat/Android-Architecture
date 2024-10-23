package com.ar.trackcrypto.crypto.presentation.coin_list

import com.ar.trackcrypto.core.domain.util.NetworkError

sealed interface CoinListEvent {
    data class Error(val error: NetworkError): CoinListEvent
}
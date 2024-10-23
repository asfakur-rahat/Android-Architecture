package com.ar.trackcrypto.crypto.data.networking

import com.ar.trackcrypto.core.data.networking.constructUrl
import com.ar.trackcrypto.core.data.networking.safeCall
import com.ar.trackcrypto.core.domain.util.NetworkError
import com.ar.trackcrypto.core.domain.util.Result
import com.ar.trackcrypto.core.domain.util.map
import com.ar.trackcrypto.crypto.data.mappers.toCoin
import com.ar.trackcrypto.crypto.data.mappers.toCoinPrice
import com.ar.trackcrypto.crypto.data.networking.dto.CoinHistoryDto
import com.ar.trackcrypto.crypto.data.networking.dto.CoinsResponseDto
import com.ar.trackcrypto.crypto.domain.Coin
import com.ar.trackcrypto.crypto.domain.CoinDataSource
import com.ar.trackcrypto.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource(
    private val httpClient: HttpClient
): CoinDataSource {

    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMillis = start
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
        val endMillis = end
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

        return safeCall<CoinHistoryDto> {
            httpClient.get(
                urlString = constructUrl("/assets/$coinId/history")
            ) {
                parameter("interval", "d1")
                parameter("start", startMillis)
                parameter("end", endMillis)
            }
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}
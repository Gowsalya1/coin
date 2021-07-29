package com.coin.api

import com.coin.app.CoinApplication
import com.coin.models.CurrencyDetailsResponse
import com.coin.models.CurrencyListResponse
import io.reactivex.Observable

class ApiService {
    private val api = CoinApplication.RETROFIT.create(Api::class.java)

    fun getAllCryptoCurrencyDetails(
        start: Int,
        limit: Int,
        sort: String
    ): Observable<CurrencyListResponse> {
        return api.getAllCurrencyDetails(start, limit, sort)

    }

    fun getAllCryptoCurrencyDetails(
        id: String
    ): Observable<CurrencyDetailsResponse> {
        return api.getCurrencyInfo(id)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ApiService()
    }
}
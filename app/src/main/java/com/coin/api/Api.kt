package com.coin.api

import com.coin.models.CurrencyDetailsResponse
import com.coin.models.CurrencyListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET(ApiPath.GET_ALL_DETAILS)
    fun getAllCurrencyDetails(
        @Query(ApiConstant.START) start: Int?,
        @Query(ApiConstant.LIMIT) limit: Int?,
        @Query(ApiConstant.SORT) sort: String?,
    ): Observable<CurrencyListResponse>

    @GET(ApiPath.GET_INFO)
    fun getCurrencyInfo(
        @Query(ApiConstant.ID) limit: String?,
    ): Observable<CurrencyDetailsResponse>
}
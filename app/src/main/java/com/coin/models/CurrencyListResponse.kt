package com.coin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrencyListResponse(
    @SerializedName("data")
    @Expose
    var data: ArrayList<CurrencyList>,
    @SerializedName("status")
    @Expose
    var status: Status
)
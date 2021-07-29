package com.coin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrencyDetailsResponse(
    @SerializedName("data")
    @Expose
    var data: Any,
    @SerializedName("status")
    @Expose
    var status: Status
)
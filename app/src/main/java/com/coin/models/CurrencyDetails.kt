package com.coin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

class CurrencyDetails {
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("name")
    @Expose
    var name: String = ""

    @SerializedName("symbol")
    @Expose
    var symbol: String = ""

    @SerializedName("slug")
    @Expose
    var slug: String = ""

    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("logo")
    @Expose
    var logo: String = ""

    @SerializedName("notice")
    @Expose
    var notice: String = ""

    @SerializedName("date_added")
    @Expose
    var date_added: String = ""

    @SerializedName("platform")
    @Expose
    var platform: Platform? = null

    @SerializedName("category")
    @Expose
    var category: String = ""

    @SerializedName("subreddit")
    @Expose
    var subreddit: String = ""

    constructor()
    constructor(data: Map<*, *>) {
        (data["id"] as Double).roundToInt().also { id = it }
        (data["name"] as String).also { name = it }
        (data["symbol"] as String).also { symbol = it }
        (data["slug"] as String).also { slug = it }
        (data["description"] as String).also { description = it }
        (data["logo"] as String).also { logo = it }
        (data["notice"] as String).also { notice = it }
        (data["date_added"] as String).also { date_added = it }
        (data["category"] as String).also { category = it }
        (data["subreddit"] as String).also { subreddit = it }
        (data["platform"] as Platform?)?.also { platform = it }
    }
}
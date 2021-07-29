package com.coin.interpreter

import com.coin.models.CurrencyList

interface OnAddListener {
    fun addToFavorite(list: CurrencyList)
    fun viewDetail(list: CurrencyList)
}

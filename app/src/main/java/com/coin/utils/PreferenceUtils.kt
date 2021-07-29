package com.coin.utils

import android.content.Context
import android.content.SharedPreferences
import com.coin.constant.PreferenceKey
import com.coin.models.CurrencyDetails
import com.coin.models.CurrencyList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceUtils {
    private var currentPreferences: SharedPreferences? = null
    private fun getSharedPreferences(context: Context?): SharedPreferences? {
        try {
            currentPreferences = context?.getSharedPreferences("Coin", Context.MODE_PRIVATE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentPreferences
    }

    private fun commit(editor: SharedPreferences.Editor) {
        if (!editor.commit()) editor.apply()
    }

    fun setCryptoList(context: Context?, currencyList: ArrayList<CurrencyList>) {
        val editor = getSharedPreferences(context)!!.edit()
        try {
            editor.putString(PreferenceKey.CRYPTO_LIST, Gson().toJson(currencyList))
        } catch (e: Exception) {
            e.printStackTrace()
            editor.putString(PreferenceKey.CRYPTO_LIST, null)
        }
        commit(editor)
    }

    fun getCryptoList(context: Context?): ArrayList<CurrencyList>? {
        try {
            val type =
                object : TypeToken<ArrayList<CurrencyList>>() {}.type//converting the json to list
            return Gson().fromJson(
                getSharedPreferences(context)?.getString(
                    PreferenceKey.CRYPTO_LIST,
                    null
                ), type
            )//returning the list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun addCryptoList(context: Context?, currencyList: ArrayList<CurrencyList>) {
        val cryptoList = getCryptoList(context)
        if (cryptoList != null) {
            cryptoList.addAll(currencyList)
            setCryptoList(context, cryptoList)
        } else setCryptoList(context, currencyList)
    }

    fun changeCryptoList(context: Context?, currencyList: CurrencyList) {
        val cryptoList = getCryptoList(context)
        if (cryptoList != null) {
            val index: Int = cryptoList.indexOfFirst { it.id == currencyList.id }
            if (index != -1)
                cryptoList[index].favorite = !currencyList.favorite
            setCryptoList(context, cryptoList)
        } else cryptoList?.let { setCryptoList(context, it) }
    }

    fun refreshCryptoList(context: Context, currencyList: ArrayList<CurrencyList>) {
        val cryptoList = getCryptoList(context)
        if (cryptoList != null) {
            cryptoList.forEach { oldCurrencyList ->
                currencyList.map { newCurrencyList ->
                    if (oldCurrencyList.id == newCurrencyList.id) {
                        newCurrencyList.favorite = oldCurrencyList.favorite
                    }
                }
            }
            setCryptoList(context, currencyList)
        } else setCryptoList(context, currencyList)
    }


    fun setCryptoDetails(id: String, context: Context?, currencyDetails: CurrencyDetails) {
        val editor = getSharedPreferences(context)!!.edit()
        try {
            editor.putString(id, GsonUtils.toJson(currencyDetails))
        } catch (e: Exception) {
            e.printStackTrace()
            editor.putString(id, null)
        }
        commit(editor)
    }

    fun getCryptoDetails(id: String, context: Context?): CurrencyDetails? {
        return try {
            val currencyDetails = GsonUtils.fromJson(
                getSharedPreferences(context)?.getString(id, ""),
                CurrencyDetails::class.java
            )
            currencyDetails
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
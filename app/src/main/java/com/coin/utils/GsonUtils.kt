package com.coin.utils

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by SoftSuave on 26-Nov-18 at 12:16:PM.
 */
object GsonUtils {
    private var gson: Gson? = null
        private get() {
            if (field == null) field = Gson()
            return field
        }

    @JvmStatic
    fun <T> fromJson(body: String?, classOfT: Class<T>?): T {
        return gson!!.fromJson(body, classOfT)
    }

    fun <T> fromJson(body: String?, typeOfT: Type?): T {
        return gson!!.fromJson(body, typeOfT)
    }

    @JvmStatic
    fun toJson(src: Any?): String {
        return gson!!.toJson(src)
    }

}
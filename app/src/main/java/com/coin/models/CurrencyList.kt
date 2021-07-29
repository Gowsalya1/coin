package com.coin.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrencyList(
    @SerializedName("id")
    @Expose
    var id: String?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("symbol")
    @Expose
    var symbol: String?,
    @SerializedName("rank")
    @Expose
    var rank: Int,
    @SerializedName("is_active")
    @Expose
    var is_active: Int,
    @SerializedName("first_historical_data")
    @Expose
    var first_historical_data: String?,
    @SerializedName("last_historical_data")
    @Expose
    var last_historical_data: String?,
    @SerializedName("platform")
    @Expose
    var platform: Platform?,
    @SerializedName("favorite")
    @Expose
    var favorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Platform::class.java.classLoader),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeInt(rank)
        parcel.writeInt(is_active)
        parcel.writeString(first_historical_data)
        parcel.writeString(last_historical_data)
        parcel.writeByte(if (favorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrencyList> {
        override fun createFromParcel(parcel: Parcel): CurrencyList {
            return CurrencyList(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyList?> {
            return arrayOfNulls(size)
        }
    }
}

package com.coin.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Platform(
    @SerializedName("id")
    @Expose
    var id: String?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("symbol")
    @Expose
    var symbol: String?,
    @SerializedName("slug")
    @Expose
    var slug: String?,
    @SerializedName("token_address")
    @Expose
    var token_address: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeString(slug)
        parcel.writeString(token_address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Platform> {
        override fun createFromParcel(parcel: Parcel): Platform {
            return Platform(parcel)
        }

        override fun newArray(size: Int): Array<Platform?> {
            return arrayOfNulls(size)
        }
    }
}
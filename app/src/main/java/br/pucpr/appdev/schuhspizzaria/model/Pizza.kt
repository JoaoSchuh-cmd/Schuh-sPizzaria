package br.pucpr.appdev.schuhspizzaria.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Pizza (
    val size : String = "",
    val flavors : String = "",
    val withEdge : Boolean = false,
    val price : Double = 0.00,
    val orderId : Long = -1,
) : Parcelable {
    var id : Long = -1

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readBoolean(),
        parcel.readDouble(),
        parcel.readLong()
    ) {

    }
    override fun describeContents(): Int {
        return 0
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(size)
        parcel.writeString(flavors)
        parcel.writeBoolean(withEdge)
        parcel.writeDouble(price)
        parcel.writeLong(orderId)
    }

    companion object CREATOR : Parcelable.Creator<Pizza> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Pizza {
            return Pizza(parcel)
        }

        override fun newArray(size: Int): Array<Pizza?> {
            return arrayOfNulls(size)
        }
    }
}
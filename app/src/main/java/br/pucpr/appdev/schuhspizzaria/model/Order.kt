package br.pucpr.appdev.schuhspizzaria.model

import android.os.Parcel
import android.os.Parcelable

data class Order (
    val price : Double = 0.00,
    val status : Int = -1,
    val date: String = "",
) : Parcelable {
    var id : Long = -1

    constructor(parcel : Parcel) : this(
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: "")

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeDouble(price)
        parcel.writeInt(status)
        parcel.writeString(date)
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.os.Parcel
import android.os.Parcelable


data class PpeItemData(

    var itemId: String,
    val itemDescription: String,
    val total: Int,
    var available: Int,
) : Parcelable {
    constructor() : this("","", 0, 0)

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemId)
        parcel.writeString(itemDescription)
        parcel.writeInt(total)
        parcel.writeInt(available)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PpeItemData> {
        override fun createFromParcel(parcel: Parcel): PpeItemData {
            return PpeItemData(parcel)
        }

        override fun newArray(size: Int): Array<PpeItemData?> {
            return arrayOfNulls(size)
        }
    }
}
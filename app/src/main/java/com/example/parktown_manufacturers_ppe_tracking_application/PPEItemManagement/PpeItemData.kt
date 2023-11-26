package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.os.Parcel
import android.os.Parcelable


data class PpeItemData(
    val itemName: String,
    val total: Int,
    val available: Int,
) : Parcelable {
    constructor() : this("", 0, 0)

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemName)
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
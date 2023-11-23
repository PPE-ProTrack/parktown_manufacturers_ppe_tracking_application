package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.os.Parcel
import android.os.Parcelable


data class PpeItemData(
    val itemId: String,
    val description: String,
    val total: Int,
    val available: Int,
    val allocationRules: String,
) : Parcelable {
    constructor() : this("", "", 0, 0, "")

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemId)
        parcel.writeString(description)
        parcel.writeInt(total)
        parcel.writeInt(available)
        parcel.writeString(allocationRules)
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
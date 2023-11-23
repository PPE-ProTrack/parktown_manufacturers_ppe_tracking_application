package com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuancePendingReturns

import android.os.Parcel
import android.os.Parcelable


data class PendingReturnsData(
    val recordId : Int,
    val empId: Int,
    val empName: String,
    val itemId: Int,
    val itemDescription: String,
    val issuanceDate: String,
    val returnDate : String,
) : Parcelable {
    constructor() : this( 0, 0, "",0,  "", "", "")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(recordId)
        parcel.writeInt(empId)
        parcel.writeString(empName)
        parcel.writeInt(itemId)
        parcel.writeString(itemDescription)
        parcel.writeString(issuanceDate)
        parcel.writeString(returnDate)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PendingReturnsData> {
        override fun createFromParcel(parcel: Parcel): PendingReturnsData {
            return PendingReturnsData(parcel)
        }

        override fun newArray(size: Int): Array<PendingReturnsData?> {
            return arrayOfNulls(size)
        }
    }
}
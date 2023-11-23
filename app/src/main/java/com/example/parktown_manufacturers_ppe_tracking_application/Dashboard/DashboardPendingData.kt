package com.example.parktown_manufacturers_ppe_tracking_application.Dashboard

import android.os.Parcel
import android.os.Parcelable


data class DashboardPendingData (
    val empName: String,
    val itemDescription: String,
    val issuanceDate: String,

    ) : Parcelable {
    constructor() : this(  "", "", "")

    constructor(parcel: Parcel) : this(

        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeString(empName)

        parcel.writeString(itemDescription)
        parcel.writeString(issuanceDate)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DashboardPendingData> {
        override fun createFromParcel(parcel: Parcel): DashboardPendingData {
            return DashboardPendingData(parcel)
        }

        override fun newArray(size: Int): Array<DashboardPendingData?> {
            return arrayOfNulls(size)
        }
    }
}
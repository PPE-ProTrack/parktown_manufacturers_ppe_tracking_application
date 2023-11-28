package com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.Infringements

import android.os.Parcel
import android.os.Parcelable


class InfringementsData (
    var empItemIdentifier: String,
    var infringementId : String,
    val empId: Int,
    val itemId: String,
    val itemDescription: String,
    var itemNotReturnedCount: Int,
) : Parcelable {
    constructor() : this("","",  0,"", "", 0 )

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",

        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),

        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(empItemIdentifier)
        parcel.writeString(infringementId)
        parcel.writeInt(empId)
        parcel.writeString(itemId)
        parcel.writeString(itemDescription)
        parcel.writeInt(itemNotReturnedCount)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InfringementsData> {
        override fun createFromParcel(parcel: Parcel): InfringementsData {
            return InfringementsData(parcel)
        }

        override fun newArray(size: Int): Array<InfringementsData?> {
            return arrayOfNulls(size)
        }
    }
}
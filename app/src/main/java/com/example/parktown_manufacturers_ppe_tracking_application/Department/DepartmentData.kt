package com.example.parktown_manufacturers_ppe_tracking_application.Department

import android.os.Parcel
import android.os.Parcelable

data class DepartmentData(

    val departmentId: Int,
    val departmentName: String,
    val totalEmployees : Int,
) : Parcelable {
    constructor() : this(0, "", 0)

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(departmentId)
        parcel.writeString(departmentName)
        parcel.writeInt(totalEmployees)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DepartmentData> {
        override fun createFromParcel(parcel: Parcel): DepartmentData {
            return DepartmentData(parcel)
        }

        override fun newArray(size: Int): Array<DepartmentData?> {
            return arrayOfNulls(size)
        }
    }
}
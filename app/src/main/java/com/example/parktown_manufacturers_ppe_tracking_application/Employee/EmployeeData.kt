package com.example.parktown_manufacturers_ppe_tracking_application.Employee

import android.os.Parcel
import android.os.Parcelable

class EmployeeData (
    val employeeId: Int,
    val employeeName: String,
    val employeeSurname: String,
    val departmentId : Int,
    val departmentName: String,
) : Parcelable {
    constructor() : this(0,"", "", 0, "")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",

        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(employeeId)
        parcel.writeString(employeeName)
        parcel.writeString(employeeSurname)
        parcel.writeInt(departmentId)
        parcel.writeString(departmentName)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmployeeData> {
        override fun createFromParcel(parcel: Parcel): EmployeeData {
            return EmployeeData(parcel)
        }

        override fun newArray(size: Int): Array<EmployeeData?> {
            return arrayOfNulls(size)
        }
    }
}
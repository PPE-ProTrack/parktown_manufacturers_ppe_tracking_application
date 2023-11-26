package com.example.parktown_manufacturers_ppe_tracking_application.Employee

import android.os.Parcel
import android.os.Parcelable
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemData

class EmployeeData (
    val employeeId: Int,
    val employeeName: String,
    val employeeSurname: String,
    val employeeFullName: String,
    val departmentId : Int,
    val departmentName: String,

) : Parcelable {
    constructor() : this(0,"", "","", 0, "")

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
//        mutableListOf<PpeItemData>().apply {
//            parcel.readList(this, PpeItemData::class.java.classLoader)
//        }
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(employeeId)
        parcel.writeString(employeeName)
        parcel.writeString(employeeSurname)
        parcel.writeString(employeeFullName)
        parcel.writeInt(departmentId)
        parcel.writeString(departmentName)
        //parcel.writeList(infringements)


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
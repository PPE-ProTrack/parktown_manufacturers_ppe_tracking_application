package com.example.parktown_manufacturers_ppe_tracking_application.Employee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.R

class EmployeeAdapter (
    private val employeeList: MutableList<EmployeeData>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.employee_cardview,
            parent, false
        )
        return EmployeeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentItem = employeeList[position]
        holder.employeeName.text = currentItem.employeeName
        holder.employeeID.text = currentItem.employeeId.toString()
        holder.departmentID.text = currentItem.departmentId.toString()

        // Set click listener for the whole item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = employeeList.size

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val employeeName: TextView = itemView.findViewById(R.id.empName_TextView)
        val employeeID: TextView = itemView.findViewById(R.id.employeeID_TextView)
        val departmentID: TextView = itemView.findViewById(R.id.departmentID_TextView)

    }
}
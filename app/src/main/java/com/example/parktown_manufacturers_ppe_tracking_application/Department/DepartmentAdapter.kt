package com.example.parktown_manufacturers_ppe_tracking_application.Department

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.R

class DepartmentAdapter (
    private val departmentList: MutableList<DepartmentData>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DepartmentAdapter.departmentViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): departmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.department_cardview,
            parent, false
        )
        return departmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: departmentViewHolder, position: Int) {
        val currentItem = departmentList[position]
        holder.departmentName.text = currentItem.departmentName
        holder.departmentPpeItemTotal.text = "Total Employees: ${currentItem.totalEmployees}"

        // Set click listener for the whole item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = departmentList.size

    class departmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val departmentName: TextView = itemView.findViewById(R.id.departmentName_TextView)
        val departmentPpeItemTotal: TextView = itemView.findViewById(R.id.departmentTotalEmp_TextView)
    }
}
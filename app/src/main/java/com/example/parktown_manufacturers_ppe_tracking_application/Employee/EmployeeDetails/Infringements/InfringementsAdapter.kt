package com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.Infringements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.R

class InfringementsAdapter (
    private val infringementList: MutableList<InfringementsData>) : RecyclerView.Adapter<InfringementsAdapter.InfringementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfringementViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.infringement_cardview,
            parent, false
        )
        return InfringementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InfringementViewHolder, position: Int) {
        val currentItem = infringementList[position]
        holder.itemName.text = currentItem.itemDescription
        holder.notReturned.text = currentItem.itemNotReturnedCount.toString()

    }
    override fun getItemCount() = infringementList.size

    class InfringementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.ppeItemName_TextView)
        val notReturned: TextView = itemView.findViewById(R.id.not_returned_TextView)


    }
}
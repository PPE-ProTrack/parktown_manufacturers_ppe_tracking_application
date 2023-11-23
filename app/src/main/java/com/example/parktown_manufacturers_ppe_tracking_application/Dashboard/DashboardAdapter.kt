package com.example.parktown_manufacturers_ppe_tracking_application.Dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.R


class DashboardAdapter(
    private val pendingReturnsList: MutableList<DashboardPendingData>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DashboardAdapter.DashboardPendingViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardPendingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dashboard_pending_returns_cardview,
            parent, false
        )
        return DashboardPendingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DashboardPendingViewHolder, position: Int) {
        val currentItem = pendingReturnsList[position]
        holder.empName.text = currentItem.empName
        holder.itemDescription.text = currentItem.itemDescription
        holder.issuanceDate.text = currentItem.issuanceDate



        // Set click listener for the whole item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = pendingReturnsList.size

    class DashboardPendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val empName: TextView = itemView.findViewById(R.id.empName_TextView)
        val itemDescription: TextView = itemView.findViewById(R.id.itemDescription_TextView)
        val issuanceDate: TextView = itemView.findViewById(R.id.date_issued_TextView)


    }
}
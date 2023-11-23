package com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuancePendingReturns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.R


class PendingReturnsAdapter (
    private val pendingReturnsList: MutableList<PendingReturnsData>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PendingReturnsAdapter.pendingReturnsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pendingReturnsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.issuance_pending_return_cardview,
            parent, false
        )
        return pendingReturnsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: pendingReturnsViewHolder, position: Int) {
        val currentItem = pendingReturnsList[position]
        holder.empName.text = currentItem.empName
        holder.itemDescription.text = currentItem.itemDescription
        holder.issuanceDate.text = currentItem.issuanceDate
        holder.returnDate.text = currentItem.returnDate


        // Set click listener for the whole item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = pendingReturnsList.size

    class pendingReturnsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val empName: TextView = itemView.findViewById(R.id.empName_TextView)
        val itemDescription: TextView = itemView.findViewById(R.id.itemDescription_TextView)
        val issuanceDate: TextView = itemView.findViewById(R.id.issuanceDate_TextView)
        val returnDate: TextView = itemView.findViewById(R.id.returnDate_TextView)

    }
}
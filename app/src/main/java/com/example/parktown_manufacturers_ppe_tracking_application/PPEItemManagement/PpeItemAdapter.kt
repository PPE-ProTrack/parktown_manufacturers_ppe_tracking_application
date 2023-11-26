package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.R


class PpeItemAdapter (
    private val ppeItemList: MutableList<PpeItemData>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PpeItemAdapter.ppeItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ppeItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.ppeitem_cardview,
            parent, false
        )
        return ppeItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ppeItemViewHolder, position: Int) {
        val currentItem = ppeItemList[position]
        holder.ppeItemName.text = currentItem.itemName
        holder.ppeItemTotal.text = "Total: ${currentItem.total}"
        holder.ppeItemAvailable.text = "Available: ${currentItem.available}"


        // Set click listener for the whole item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = ppeItemList.size

    class ppeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ppeItemName: TextView = itemView.findViewById(R.id.ppeItemName_TextView)
        val ppeItemTotal: TextView = itemView.findViewById(R.id.ppeItemTotal_TextView)
        val ppeItemAvailable: TextView = itemView.findViewById(R.id.ppeItemAvailable_TextView)


    }
}
package com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.R


class RecordsAdapter (
    private val RecordsList: MutableList<RecordsData>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.issuance_records_cardview,
            parent, false
        )
        return RecordsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        val currentItem = RecordsList[position]
        holder.empName.text = currentItem.empName
        holder.itemDescription.text = currentItem.itemDescription
        holder.issuanceDate.text = currentItem.issuanceDate
        holder.returnDate.text = currentItem.returnDate

        // Set click listener for the whole item
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
    override fun getItemCount() = RecordsList.size

    class RecordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val empName: TextView = itemView.findViewById(R.id.empName_TextView)
        val itemDescription: TextView = itemView.findViewById(R.id.itemDescription_TextView)
        val issuanceDate: TextView = itemView.findViewById(R.id.issuanceDate_TextView)
        val returnDate: TextView = itemView.findViewById(R.id.returnDate_TextView)
        //val testButton: TextView = itemView.findViewById(R.id.testbutton)
    }
}
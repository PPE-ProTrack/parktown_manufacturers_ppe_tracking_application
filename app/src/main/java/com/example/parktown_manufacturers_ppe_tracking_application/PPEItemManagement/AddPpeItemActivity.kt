package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPpeItemActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var saveButton: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ppe_item)

        backButton = findViewById(R.id.add_ppe_backbutton)
        saveButton = findViewById(R.id.btnSave)
        progressbar = findViewById(R.id.progressBar)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference.child("ppeItems")
        backButton.setOnClickListener {
            val intent = Intent(this, PpeItemsActivity::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            progressbar.bringToFront()

            // Get data from EditText fields
//            val itemId = findViewById<EditText>(R.id.ppeItemId_EditText).text.toString()
            val itemName = findViewById<EditText>(R.id.ppeItemName_EditText).text.toString()
            val itemQuantity = findViewById<EditText>(R.id.ppeItemQuantity_EditText).text.toString().toInt()

            // Create PpeItemData object
            val ppeItemData = PpeItemData(itemName, itemQuantity, itemQuantity)

            // Save to Firebase
            val key = databaseReference.push().key
            key?.let {
                // Set the item with the generated key
                databaseReference.child(it).setValue(ppeItemData)
            }
            progressbar.visibility = View.GONE

            val intent = Intent(this, PpeItemsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
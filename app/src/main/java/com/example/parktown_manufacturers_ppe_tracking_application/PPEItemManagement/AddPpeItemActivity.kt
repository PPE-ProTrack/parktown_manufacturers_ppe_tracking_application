package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddPpeItemActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var saveButton: Button
    private lateinit var itemName: EditText
    private lateinit var itemQuantity: EditText
    private lateinit var database: FirebaseDatabase
    private var counter = 0

    companion object {
        private const val COUNTER_KEY = "counter_key"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ppe_item)

        itemQuantity = findViewById(R.id.ppeItemQuantity_EditText)
        itemName = findViewById(R.id.ppeItemName_EditText)
        saveButton= findViewById(R.id.btnSave)
        backButton = findViewById(R.id.add_ppe_backbutton)
        database = FirebaseDatabase.getInstance()

        saveButton.setOnClickListener {
            // Get PPE name from EditText
            val itemNameText = itemName.text.toString().trim()

            // Convert itemQuantity to Int
            val itemQuantityStr = itemQuantity.text.toString().trim()
            val itemQuantity = if (itemQuantityStr.isEmpty()) 0 else itemQuantityStr.toInt()

            // Create a PpeItemData object without setting itemId
            val ppeItemData = PpeItemData("", itemNameText, itemQuantity, itemQuantity)

            // Save PPE item to Firebase
            savePpeItemToFirebase(ppeItemData)
        }


        backButton.setOnClickListener {
            val intent = Intent(this, PpeItemsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COUNTER_KEY, counter)
    }
    private fun savePpeItemToFirebase(ppeItemData: PpeItemData) {
        val dbReference = database.getReference("PpeItems")
        val newItemReference = dbReference.push() // Generate a unique key for the new item

        // Set the itemId explicitly before setting other values
        val itemId = newItemReference.key
        if (itemId != null) {
            ppeItemData.itemId = itemId
        }

        // Set the values of the PpeItemData object to the generated reference
        newItemReference.child("itemId").setValue(itemId)
        newItemReference.child("itemDescription").setValue(ppeItemData.itemDescription)
        newItemReference.child("total").setValue(ppeItemData.total)
        newItemReference.child("available").setValue(ppeItemData.available)
        // Add more fields as needed

        newItemReference.setValue(ppeItemData).addOnSuccessListener {
            // Additional actions after successful save
            // For example, you might want to update UI, show a toast, etc.

            val intent = Intent(this, PpeItemsActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "PPE item added successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to add PPE item: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


}
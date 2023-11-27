package com.example.parktown_manufacturers_ppe_tracking_application.Department

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.firestore.FirebaseFirestore

class AddDepartmentActivity : AppCompatActivity() {

    private lateinit var editTextDepartmentName: EditText
    private lateinit var buttonAddDepartment: Button

    // Access a Cloud Firestore instance
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_department)

        // Initialize views
        editTextDepartmentName = findViewById(R.id.editTextDepartmentName)
        buttonAddDepartment = findViewById(R.id.buttonAddDepartment)

        // Set click listener for the add department button
        buttonAddDepartment.setOnClickListener {
            // Call a method to handle the addition of the department
            addDepartment()
        }
    }

    // Method to handle the addition of a department
    private fun addDepartment() {
        // Get the department name from the EditText
        val departmentName = editTextDepartmentName.text.toString().trim()

        // Check if the department name is not empty
        if (departmentName.isNotEmpty()) {
            // Save the department name to Firestore
            saveDepartmentToFirestore(departmentName)

            // Display a success message
            Toast.makeText(this, "Department added successfully", Toast.LENGTH_SHORT).show()
        } else {
            // Display an error message for an empty department name
            Toast.makeText(this, "Please enter a department name", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to save the department name to Firestore
    private fun saveDepartmentToFirestore(departmentName: String) {
        // Create a new document with a generated ID
        db.collection("departments")
            .add(mapOf("name" to departmentName))
            .addOnSuccessListener { documentReference ->
                // Log success or perform additional actions if needed
            }
            .addOnFailureListener { e ->
                // Log error or perform additional error handling if needed
                Toast.makeText(
                    this,
                    "Error adding department to Firestore",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}


package com.example.parktown_manufacturers_ppe_tracking_application.Department

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import java.util.HashSet

class AddDepartmentActivity : AppCompatActivity() {

    private lateinit var editTextDepartmentName: EditText
    private lateinit var buttonAddDepartment: Button

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
            // Save the department name to SharedPreferences
            saveDepartment(departmentName)

            // Display a success message
            Toast.makeText(this, "Department added successfully", Toast.LENGTH_SHORT).show()
        } else {
            // Display an error message for an empty department name
            Toast.makeText(this, "Please enter a department name", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to save the department name to SharedPreferences
    private fun saveDepartment(departmentName: String) {
        val preferences: SharedPreferences = getSharedPreferences("Departments", MODE_PRIVATE)
        val departmentSet: MutableSet<String> =
            preferences.getStringSet("departmentSet", HashSet()) ?: HashSet()
        departmentSet.add(departmentName)

        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putStringSet("departmentSet", departmentSet)
        editor.apply()
    }
}

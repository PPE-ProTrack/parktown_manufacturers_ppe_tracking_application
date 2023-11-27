package com.example.parktown_manufacturers_ppe_tracking_application.Department


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*

class AddDepartmentActivity : AppCompatActivity() {

    private lateinit var departmentNameEditText : EditText
    private lateinit var btnSave: Button
    private lateinit var toolbar : Toolbar
    private lateinit var progressbar: ProgressBar
    private lateinit var departmentsDatabaseReference: DatabaseReference
    private lateinit var backButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_department)


        toolbar = findViewById(R.id.toolbar)

         progressbar = findViewById(R.id.progressBar)
         progressbar.bringToFront()
         progressbar.visibility = View.GONE

        // Initialize EditTexts
        departmentNameEditText = findViewById(R.id.department_name_edittext)


        // Initialize Button
        btnSave = findViewById(R.id.saveDeptBtn)
        backButton = findViewById(R.id.add_department_backbutton)

        // Initialize Firebase Database reference
        departmentsDatabaseReference = FirebaseDatabase.getInstance().reference.child("departments")


        backButton.setOnClickListener {
            val intent = Intent(this, DepartmentActivity::class.java)
            startActivity(intent)
        }

        toolbar.setTitle("Add Department")
        toolbar.titleMarginStart= 150
        setSupportActionBar(toolbar)

        btnSave.setOnClickListener {
            saveBtn()
        }



    }

    private fun saveBtn() {
        val departmentName = departmentNameEditText.text.toString().trim()

        if (departmentName.isNotEmpty()) {
            generateUniqueDepartmentId { newDepartmentId ->
                val totalEmployees = 0 // Initialize totalEmployees to zero

                val departmentData = DepartmentData(newDepartmentId, departmentName, totalEmployees)

                departmentsDatabaseReference.child(newDepartmentId.toString()).setValue(departmentData)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Department added successfully
                            showToast("Department added successfully")
                            finish() // Close the activity after successful addition
                        } else {
                            showToast("Failed to add department")
                        }
                    }
            }
        } else {
            showToast("Department name cannot be empty")
        }
    }

    private fun generateUniqueDepartmentId(callback: (Int) -> Unit) {
        // Get the highest department ID from the database
        departmentsDatabaseReference.orderByKey().limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val lastDepartmentId = dataSnapshot.children.firstOrNull()?.key?.toIntOrNull()
                    val newDepartmentId = getNextDepartmentId(lastDepartmentId)
                    callback.invoke(newDepartmentId)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun getNextDepartmentId(lastDepartmentId: Int?): Int {
        val lastIdInt = lastDepartmentId?: 0
        val nextIdInt = lastIdInt + 1000
        return nextIdInt
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    

    
}


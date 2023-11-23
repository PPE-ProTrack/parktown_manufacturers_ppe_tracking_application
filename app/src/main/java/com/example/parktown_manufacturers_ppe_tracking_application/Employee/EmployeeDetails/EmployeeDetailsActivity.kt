package com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeData
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var empIdTextView : TextView
    private lateinit var empNameTextView : TextView
    private lateinit var empSurnameTextView : TextView
    private lateinit var empDepartmentIdTextView : TextView
    private lateinit var empDepartmentNameTextView : TextView

    // Reference to the Firebase Realtime Database
    private lateinit var databaseReference: DatabaseReference

    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        // Initialize EditTexts
        empIdTextView = findViewById(R.id.employee_id_textview)
        empNameTextView = findViewById(R.id.employee_name_textview)
        empSurnameTextView = findViewById(R.id.employee_surname_textview)
        empDepartmentIdTextView = findViewById(R.id.department_id_textview)
        empDepartmentNameTextView = findViewById(R.id.department_name_textview)

        backButton = findViewById(R.id.employee_details_backbutton)

        backButton.setOnClickListener {
            val intent = Intent(this, EmployeeActivity::class.java)
            startActivity(intent)
        }

        // Retrieve empId from the Intent
        val empId = intent.getIntExtra("empId", 0)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("employees")

        // Query to fetch the specific employee using empId
        val query: Query = databaseReference.orderByChild("employeeId").equalTo(empId.toDouble())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if there is a matching employee
                if (dataSnapshot.exists()) {
                    for (employeeSnapshot in dataSnapshot.children) {
                        // Parse the employee details from the dataSnapshot
                        val employee = employeeSnapshot.getValue(EmployeeData::class.java)

                        // Update the TextViews with the employee details
                        employee?.let {
                            empIdTextView.text = it.employeeId.toString()
                            empNameTextView.text = it.employeeName
                            empSurnameTextView.text = it.employeeSurname
                            empDepartmentIdTextView.text = it.departmentId.toString()
                            empDepartmentNameTextView.text = it.departmentName
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                // You can log the error or show a message to the user
            }
        })
    }
}
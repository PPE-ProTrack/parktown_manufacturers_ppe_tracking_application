package com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeData
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.Infringements.InfringementsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var empIdTextView : TextView
    private lateinit var empNameTextView : TextView
    private lateinit var empSurnameTextView : TextView
    private lateinit var empFullNameTextView : TextView
    private lateinit var empDepartmentNameTextView : TextView
    private lateinit var toolbar : Toolbar
    // Reference to the Firebase Realtime Database
    private lateinit var databaseReference: DatabaseReference

    private lateinit var backButton: Button
    private lateinit var empInfringementsBtn: Button

    private var empName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        // Initialize EditTexts
        toolbar = findViewById(R.id.toolbar)
        empIdTextView = findViewById(R.id.employee_id_textview)
        empNameTextView = findViewById(R.id.employee_name_textview)
        empSurnameTextView = findViewById(R.id.employee_surname_textview)
        empFullNameTextView = findViewById(R.id.employee_full_name_textview)
        empDepartmentNameTextView = findViewById(R.id.department_name_textview)

        backButton = findViewById(R.id.employee_details_backbutton)
        empInfringementsBtn = findViewById(R.id.emp_infringements_btn)

        // Retrieve empId from the Intent
        val empId = intent.getIntExtra("empId", 0)


        empInfringementsBtn.setOnClickListener {

            // Create an intent to start EmployeeDetailsActivity
            val intent = Intent(this, InfringementsActivity::class.java)

            // Pass the empId as an extra to EmployeeDetailsActivity
            intent.putExtra("empId", empId)

            // Start EmployeeDetailsActivity
            startActivity(intent)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, EmployeeActivity::class.java)
            startActivity(intent)
        }
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
                            empFullNameTextView.text = it.employeeFullName
                            empDepartmentNameTextView.text = it.departmentName
                            empName = it.employeeFullName

                            // setup toolbar
                            toolbar.title = empName
                            toolbar.titleMarginStart= 150
                            setSupportActionBar(toolbar)
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
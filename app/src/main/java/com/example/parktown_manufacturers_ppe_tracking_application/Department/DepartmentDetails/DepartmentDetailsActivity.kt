package com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeAdapter
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeData
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.EmployeeDetailsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class DepartmentDetailsActivity : AppCompatActivity(), EmployeeAdapter.OnItemClickListener {
    private lateinit var backButton: Button

    private lateinit var toolbar : Toolbar
    private lateinit var progressbar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var department_name_TextView : TextView
    private lateinit var employee_count_TextView : TextView


    // Reference to the Firebase Realtime Database
    private lateinit var employeeDatabaseReference: DatabaseReference
    private lateinit var departmentDatabaseReference: DatabaseReference
    private lateinit var emptyMessage: TextView

    var employeeList = mutableListOf<EmployeeData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department_details)

        toolbar = findViewById(R.id.toolbar)

        progressbar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.dept_emp_RecyclerView)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE
        emptyMessage = findViewById(R.id.empty_message)
        department_name_TextView = findViewById(R.id.department_name_TextView)
        employee_count_TextView = findViewById(R.id.employee_count_TextView)
        backButton = findViewById(R.id.department_details_backbutton)


        //val textView = findViewById<TextView>(R.id.department_name_edittext)


        // Retrieve empId and deptName from the Intent
        val deptId = intent.getIntExtra("deptId", 0 ) // Provide a default value of 0
        val deptName = intent.getStringExtra("deptName")

        // Use Log to check the retrieved values
        Log.d("deptId", deptId.toString())
        Log.d("deptName", deptName ?: "DeptName is null")

        // Check if deptName is not null before applying SpannableString
        deptName?.let {
            val spannableString = SpannableString(it)
            spannableString.setSpan(UnderlineSpan(), 0, it.length, 0)
            department_name_TextView.text = spannableString
        }
        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase Database reference
        employeeDatabaseReference = FirebaseDatabase.getInstance().reference.child("employees")

        // Fetch and populate employees based on deptId
        if (deptId != null) {
            fetchEmployees(deptId)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, DepartmentActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchEmployees(deptId: Int) {
        // Query the database to fetch employees with the specified deptId
        employeeDatabaseReference.orderByChild("departmentId").equalTo(deptId.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Initialize count
                    var count = 0

                    for (employeeSnapshot in dataSnapshot.children) {
                        val employeeData = employeeSnapshot.getValue(EmployeeData::class.java)
                        if (employeeData != null) {
                            employeeList.add(employeeData)
                            Log.d("employeeData", employeeData.toString())
                            Log.d("employeeList", employeeList.toString())
                            count++
                            Log.d("count", count.toString())
                        }
                    }

                    // Update count in TextView
                    employee_count_TextView.text = count.toString()

                    // Update RecyclerView adapter with the fetched data
                    updateRecyclerView(employeeList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    progressbar.visibility = View.GONE
                    emptyMessage.text = "Error fetching employee data"
                }
            })
    }

    private fun updateRecyclerView(employeeList: MutableList<EmployeeData>) {
        progressbar.visibility = View.GONE

        if (employeeList.isNotEmpty()) {
            emptyMessage.visibility = View.GONE
            recyclerView.adapter = EmployeeAdapter(employeeList, this)
        } else {
            emptyMessage.visibility = View.VISIBLE
            emptyMessage.text = "No employees found for this department"
        }
    }
    override fun onItemClick(position: Int) {
        // Get the selected employee
        val selectedEmployee = employeeList[position]

        // Create an intent to start EmployeeDetailsActivity
        val intent = Intent(this, EmployeeDetailsActivity::class.java)

        // Pass the empId as an extra to EmployeeDetailsActivity
        intent.putExtra("empId", selectedEmployee.employeeId)

        // Start EmployeeDetailsActivity
        startActivity(intent)
    }
}
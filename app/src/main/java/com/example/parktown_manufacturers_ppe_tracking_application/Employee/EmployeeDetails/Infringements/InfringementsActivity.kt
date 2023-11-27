package com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.Infringements

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeAdapter
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeData
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.EmployeeDetailsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class InfringementsActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var progressbar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var backButton: Button

    private lateinit var infringementsAdapter: InfringementsAdapter
    private val infringementList: MutableList<InfringementsData> = mutableListOf()

    private lateinit var infringementsReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infringements)

        toolbar = findViewById(R.id.toolbar)
        backButton = findViewById(R.id.infringement_backbutton)
        progressbar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.infringement_RecyclerView)

        progressbar.visibility = View.VISIBLE
        progressbar.bringToFront()
        emptyMessage = findViewById(R.id.empty_message)

        // Retrieve empId from the Intent
        val empId = intent.getIntExtra("empId", 0)

        toolbar.setTitle("Infringements")
        toolbar.titleMarginStart= 150
        setSupportActionBar(toolbar)

        backButton.setOnClickListener {


            // Create an intent to start EmployeeDetailsActivity
            val intent = Intent(this, EmployeeDetailsActivity::class.java)

            // Pass the empId as an extra to EmployeeDetailsActivity
            intent.putExtra("empId", empId)

            // Start EmployeeDetailsActivity
            startActivity(intent)
        }

            // Assuming you have a reference to your Firebase Database
            infringementsReference = FirebaseDatabase.getInstance().getReference("employee_infringements")

            // Fetch infringements based on empId
            fetchInfringements(empId)
        }

        private fun fetchInfringements(empId: Int) {
            infringementsReference.orderByChild("empId").equalTo(empId.toDouble())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        infringementList.clear()

                        for (infringementSnapshot in dataSnapshot.children) {
                            val infringement = infringementSnapshot.getValue(InfringementsData::class.java)

                            if (infringement != null) {
                                infringementList.add(infringement)

                            }
                        }

                        // Update UI based on the size of infringementList
                        if (infringementList.isEmpty()) {
                            Log.d("list3",infringementList.toString() )
                            progressbar.visibility = View.GONE
                            emptyMessage.visibility = View.VISIBLE
                            emptyMessage.text = "No infringements found"
                        } else {

                            populateRecycler()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error

                        progressbar.visibility = View.GONE
                        emptyMessage.text = "Error fetching data"
                    }
                })
        }

//        private fun updateUI() {
//            progressbar.visibility = View.GONE
//            if (infringementList.isEmpty()) {
//                emptyMessage.text = "No infringements found"
//            } else {
//                emptyMessage.text = ""
//            }
//
//            infringementsAdapter.notifyDataSetChanged()
//        }
//
//    private fun fetchEmployees() {
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val employees = mutableListOf<EmployeeData>()
//                for (employeeSnapshot in dataSnapshot.children) {
//                    val employee = employeeSnapshot.getValue(EmployeeData::class.java)
//                    employee?.let { employees.add(it) }
//                }
//
//                // Update EmployeeManager.employeeList with the fetched employees
//                EmployeeActivity.EmployeeManager.employeeList = employees
//
//                // Populate the RecyclerView
//                populateRecycler()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle the error
//                // You can log the error or show a message to the user
//            }
//        })
//    }

    private fun populateRecycler(){
        Log.d("list", infringementList.toString())
        infringementsAdapter = InfringementsAdapter(infringementList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = infringementsAdapter
        progressbar.visibility = View.GONE


        infringementsAdapter.notifyDataSetChanged()
    }

//        // Get the selected employee
//        val selectedEmployee = EmployeeActivity.EmployeeManager.employeeList[position]
//
//        // Create an intent to start EmployeeDetailsActivity
//        val intent = Intent(this, EmployeeDetailsActivity::class.java)
//
//        // Pass the empId as an extra to EmployeeDetailsActivity
//        intent.putExtra("empId", selectedEmployee.employeeId)
//
//        // Start EmployeeDetailsActivity
//        startActivity(intent)

    }
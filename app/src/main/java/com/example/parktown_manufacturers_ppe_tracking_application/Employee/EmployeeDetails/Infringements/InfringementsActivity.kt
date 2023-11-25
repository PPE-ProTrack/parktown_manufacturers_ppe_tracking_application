package com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.Infringements

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.EmployeeDetailsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class InfringementsActivity : AppCompatActivity(), InfringementsAdapter.OnItemClickListener {

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
        backButton = findViewById(R.id.add_employee_backbutton)
        progressbar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.infringement_RecyclerView)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE
        emptyMessage = findViewById(R.id.empty_message)

        // Retrieve empId from the Intent
        val empId = intent.getIntExtra("empId", 0)

        toolbar.setTitle("Infringements")

        setSupportActionBar(toolbar)

        backButton.setOnClickListener {
            val intent = Intent(this, EmployeeDetailsActivity::class.java)
            startActivity(intent)
        }



            infringementsAdapter = InfringementsAdapter(infringementList, this)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = infringementsAdapter

            // Assuming you have a reference to your Firebase Database
            infringementsReference = FirebaseDatabase.getInstance().getReference("employee_infringements")

            // Fetch infringements based on empId
            fetchInfringements(empId)
        }

        private fun fetchInfringements(empId: Int) {
            infringementsReference.orderByChild("empId").equalTo(empId.toDouble())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        infringementList.clear()

                        for (infringementSnapshot in dataSnapshot.children) {
                            val infringement = infringementSnapshot.getValue(InfringementsData::class.java)
                            if (infringement != null) {
                                infringementList.add(infringement)
                            }
                        }

                        updateUI()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error
                        progressbar.visibility = View.GONE
                        emptyMessage.text = "Error fetching data"
                    }
                })
        }

        private fun updateUI() {
            progressbar.visibility = View.GONE
            if (infringementList.isEmpty()) {
                emptyMessage.text = "No infringements found"
            } else {
                emptyMessage.text = ""
            }

            infringementsAdapter.notifyDataSetChanged()
        }
    override fun onItemClick(position: Int) {
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
    }
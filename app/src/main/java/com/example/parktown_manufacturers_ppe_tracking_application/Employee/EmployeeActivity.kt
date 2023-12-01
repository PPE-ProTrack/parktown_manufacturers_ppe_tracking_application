package com.example.parktown_manufacturers_ppe_tracking_application.Employee



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.Dashboard.DashboardActivity

import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity.EmployeeManager.employeeList
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.EmployeeDetailsActivity

import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Profile.ProfileActivity

import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class EmployeeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    EmployeeAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressbar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var AddEmployee_FloatingActionButton: FloatingActionButton
    private lateinit var auth: FirebaseAuth

    // Reference to the Firebase Realtime Database
    private lateinit var databaseReference: DatabaseReference

    private lateinit var emptyMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        auth = Firebase.auth
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.employee_drawer)
        progressbar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.employee_RecyclerView)
        AddEmployee_FloatingActionButton = findViewById(R.id.AddEmployee_FloatingActionButton)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE
        emptyMessage = findViewById(R.id.empty_message)

        AddEmployee_FloatingActionButton.setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }

        //get current user
        val currentUser = auth.currentUser
        toolbar.setTitle("Employees")

        setSupportActionBar(toolbar)

        //The drawer navigation view
        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black))
        toggle.getDrawerArrowDrawable().barLength = 100.0F
        toggle.getDrawerArrowDrawable().barThickness = 10.0F
        toggle.getDrawerArrowDrawable().gapSize = 20.0F
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        // Find the TextView within the header view
        val headerView = navigationView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<Button>(R.id.navbar_username)

        // Update the content of the TextView
        if (currentUser != null) {
            val fullName: String? = currentUser.displayName
            val firstName = fullName?.split(" ")?.getOrNull(0)
            usernameTextView.text = firstName
        }
        usernameTextView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        navigationView.setCheckedItem(R.id.nav_employee)


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("employees")

        // Fetch employees from Firebase and populate the RecyclerView
        fetchEmployees()

    }

    private fun fetchEmployees() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val employees = mutableListOf<EmployeeData>()

                for (employeeSnapshot in dataSnapshot.children) {
                    val employee = employeeSnapshot.getValue(EmployeeData::class.java)
                    employee?.let { employees.add(it) }
                }

                // Sort employees alphabetically based on the 'name' property
                val sortedEmployees = employees.sortedBy { it.employeeName }.toMutableList()

                // Update EmployeeManager.employeeList with the fetched employees
                employeeList = sortedEmployees

                // Populate the RecyclerView
                populateRecycler()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                // You can log the error or show a message to the user
            }
        })
    }

    private fun populateRecycler(){
        recyclerView.adapter = EmployeeAdapter(employeeList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressbar.visibility = View.GONE

        // Check if the adapter is empty and show the empty message if it is
        if (employeeList.isEmpty()) {
            emptyMessage.visibility = View.VISIBLE
        } else {
            emptyMessage.visibility = View.GONE
        }
    }

    object EmployeeManager{
        var employeeList: MutableList<EmployeeData> = mutableListOf()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.nav_dashboard -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_ppe_items -> {
//                Toast.makeText(this, "Feature will be Activated in next Update", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PpeItemsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_departments -> {
                val intent = Intent(this, DepartmentActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_employee -> {

            }
            R.id.nav_issuance -> {
                val intent = Intent(this, RecordsActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
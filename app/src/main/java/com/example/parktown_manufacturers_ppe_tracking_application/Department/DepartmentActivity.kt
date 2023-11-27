package com.example.parktown_manufacturers_ppe_tracking_application.Department

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity.departmentManager.departmentList
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentDetails.DepartmentDetailsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.AddEmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeAdapter
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeData
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.EmployeeDetailsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuancePendingReturns.PendingReturnsActivity
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


class DepartmentActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DepartmentAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressbar: ProgressBar
    private lateinit var departmentsDatabaseReference: DatabaseReference
    private lateinit var AddDepartment_FloatingActionButton: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    private lateinit var emptyMessage: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department)

        auth = Firebase.auth
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.department_drawerLayout)
        progressbar = findViewById(R.id.progressBar)
        emptyMessage = findViewById(R.id.empty_message)
        AddDepartment_FloatingActionButton = findViewById(R.id.addDepartment_FloatingActionButton)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE
        // Initialize Firebase Database reference
        departmentsDatabaseReference = FirebaseDatabase.getInstance().reference.child("departments")
        recyclerView = findViewById(R.id.department_RecyclerView)

        AddDepartment_FloatingActionButton.setOnClickListener {
            val intent = Intent(this, AddDepartmentActivity::class.java)
            startActivity(intent)
        }
//get current user
        val currentUser = auth.currentUser
        toolbar.setTitle("Departments")
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
            usernameTextView.text = currentUser.email
        }
        usernameTextView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        navigationView.setCheckedItem(R.id.nav_dashboard)


        fetchDepartments()

    }

    object departmentManager{
        var departmentList: MutableList<DepartmentData> = mutableListOf()
    }



    private fun fetchDepartments() {
        departmentsDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val departments = mutableListOf<DepartmentData>()
                for (departmentSnapshot in dataSnapshot.children) {
                    val department = departmentSnapshot.getValue(DepartmentData::class.java)
                    department?.let { departments.add(it) }
                }

                // Update EmployeeManager.employeeList with the fetched employees
                departmentList  = departments

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
        recyclerView.adapter = DepartmentAdapter(departmentList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressbar.visibility = View.GONE

        // Check if the adapter is empty and show the empty message if it is
        if (departmentList.isEmpty()) {
            emptyMessage.visibility = View.VISIBLE
        } else {
            emptyMessage.visibility = View.GONE
        }
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

            }
            R.id.nav_employee -> {
                val intent = Intent(this, EmployeeActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_issuance -> {
                val intent = Intent(this, RecordsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                //val intent = Intent(this, SettingsActivity::class.java)
                //startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemClick(position: Int) {
        // Get the selected employee
        val selectedDepartment = departmentList[position]
        Log.d("departmentdetails", selectedDepartment.toString())

        // Create an intent to start EmployeeDetailsActivity
        val intent = Intent(this, DepartmentDetailsActivity::class.java)

        // Pass the empId as an extra to EmployeeDetailsActivity
        intent.putExtra("deptId", selectedDepartment.departmentId)
        // Pass the empId as an extra to EmployeeDetailsActivity
        intent.putExtra("deptName", selectedDepartment.departmentName)

        Log.d("departmentdetails", selectedDepartment.departmentId.toString())
        // Start EmployeeDetailsActivity
        startActivity(intent)
    }
}
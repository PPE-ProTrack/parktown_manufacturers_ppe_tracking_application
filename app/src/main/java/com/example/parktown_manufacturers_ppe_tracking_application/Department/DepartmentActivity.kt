package com.example.parktown_manufacturers_ppe_tracking_application.Department

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.Dashboard.DashboardActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentDetails.DepartmentDetailsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuancePendingReturns.PendingReturnsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.navigation.NavigationView


class DepartmentActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DepartmentAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department)

        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.department_drawerLayout)
        progressbar = findViewById(R.id.progressBar)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE

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

        val recyclerView: RecyclerView = findViewById(R.id.department_RecyclerView)

        recyclerView.adapter = DepartmentAdapter(departmentManager.departmentList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressbar.visibility = View.GONE
    }

    object departmentManager{
        val departmentList: MutableList<DepartmentData> = mutableListOf(
            DepartmentData(
                departmentId = "1",
                departmentName = "Welding",
                allocationRules = "Rules for Item 1a",
                totalEmployees = 53
            ),
            DepartmentData(
                departmentId = "4",
                departmentName = "Drilling",
                allocationRules = "Rules for Item 1",
                totalEmployees = 45
            ),
            DepartmentData(
                departmentId = "3",
                departmentName = "Cutting and polishing",
                allocationRules = "Rules for Item 1",
                totalEmployees = 55
            ),
            DepartmentData(
                departmentId = "2",
                departmentName = "Cleaning",
                allocationRules = "Rules for Item 1",
                totalEmployees = 5
            ),
            DepartmentData(
                departmentId = "2",
                departmentName = "Cleaning",
                allocationRules = "Rules for Item 1",
                totalEmployees = 5
            ),
            DepartmentData(
                departmentId = "2",
                departmentName = "Cleaning",
                allocationRules = "Rules for Item 1",
                totalEmployees = 5
            ),
            DepartmentData(
                departmentId = "2",
                departmentName = "Cleaning",
                allocationRules = "Rules for Item 1",
                totalEmployees = 5
            ),
        )
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
                val intent = Intent(this, PendingReturnsActivity::class.java)
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
        val intent = Intent(this, DepartmentDetailsActivity::class.java)
        startActivity(intent)
    }
}
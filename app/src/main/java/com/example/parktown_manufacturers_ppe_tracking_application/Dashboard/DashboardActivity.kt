package com.example.parktown_manufacturers_ppe_tracking_application.Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuancePendingReturns.PendingReturnsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.navigation.NavigationView


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DashboardAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var pendingProgressbar: ProgressBar
    private lateinit var availableProgressbar: ProgressBar
    private lateinit var pendingSeeMore: Button
    private lateinit var availableSeeMore: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.dashboard_drawerLayout)
        pendingProgressbar = findViewById(R.id.pending_progressBar)
        pendingProgressbar.bringToFront()
        availableProgressbar = findViewById(R.id.available_progressBar)
        availableProgressbar.bringToFront()
        //progressbar.visibility = View.VISIBLE
        pendingSeeMore = findViewById(R.id.pendingSeeMore_Button)
        availableSeeMore = findViewById(R.id.availableSeeMore_Button)



        toolbar.setTitle("Dashboard")
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

        val pendingRecyclerView: RecyclerView = findViewById(R.id.dashboard_pending_RecyclerView)
        val AvailablerecyclerView: RecyclerView = findViewById(R.id.dashboard_available_RecyclerView)

        pendingSeeMore.setOnClickListener {

            pendingRecyclerView.visibility = View.VISIBLE
        }
        pendingRecyclerView.adapter = DashboardAdapter(DashboardPendingManager.DashboardPendingList, this)
        pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        pendingProgressbar.visibility = View.GONE

        availableSeeMore.setOnClickListener {

            AvailablerecyclerView.visibility = View.VISIBLE
        }
        AvailablerecyclerView.adapter = DashboardAdapter(DashboardPendingManager.DashboardPendingList, this)
        AvailablerecyclerView.layoutManager = LinearLayoutManager(this)
        availableProgressbar.visibility = View.GONE


        //progressbar.visibility = View.GONE

    }
    object DashboardPendingManager{
        val DashboardPendingList: MutableList<DashboardPendingData> = mutableListOf(
            DashboardPendingData(
                empName = "Ndumiso Zwane",
                itemDescription = "Skull cap",
                issuanceDate = "24 April 2023",
            ),
            DashboardPendingData(
                empName = "Uzair Cotwall",
                itemDescription = "Overralls",
                issuanceDate = "20 April 2023",
            ),
            DashboardPendingData(

                empName = "Halalisani Mdlalose",
                itemDescription = "Helmet",
                issuanceDate = "15 April 2023",
            ),
            DashboardPendingData(
                empName = "Mufhatutshedzwa Todani",
                itemDescription = "Skull Cap",
                issuanceDate = "12 April 2023",
            ),
            DashboardPendingData(
                empName = "Mufhatutshedzwa Todani",
                itemDescription = "Skull Cap",
                issuanceDate = "12 April 2023",
            ),
            DashboardPendingData(
                empName = "Mufhatutshedzwa Todani",
                itemDescription = "Skull Cap",
                issuanceDate = "12 April 2023",
            ),
            DashboardPendingData(
                empName = "Mufhatutshedzwa Todani",
                itemDescription = "Skull Cap",
                issuanceDate = "12 April 2023",
            ),
        )

    }
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.nav_dashboard -> {
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
        TODO("Not yet implemented")
    }

}
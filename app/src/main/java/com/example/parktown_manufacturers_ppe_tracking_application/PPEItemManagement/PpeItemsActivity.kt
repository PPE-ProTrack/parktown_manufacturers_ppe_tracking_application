package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

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
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuancePendingReturns.PendingReturnsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class PpeItemsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , PpeItemAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppe_items)

        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.ppeItem_drawerLayout)
        progressbar = findViewById(R.id.progressBar)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE

        //Setting the name of the page in the toolbar
        toolbar.setTitle("PPE Items")
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

        val recyclerView: RecyclerView = findViewById(R.id.ppeItem_RecyclerView)

        recyclerView.adapter = PpeItemAdapter(ppeItemsManager.ppeItemsList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressbar.visibility = View.GONE

        val fabAddObservation: FloatingActionButton = findViewById(R.id.AddPpeItems_FloatingActionButton)
        fabAddObservation.setOnClickListener {
            // Handle the click event for adding new observations
            startActivity(Intent(this, AddPpeItemActivity::class.java))
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
//                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    object ppeItemsManager{

        val ppeItemsList: MutableList<PpeItemData> = mutableListOf(
            PpeItemData(
                itemId = "1",
                description = "Item 1 Description",
                total = 10,
                available = 5,
                allocationRules = "Rules for Item 1"
            ),
            PpeItemData(
                itemId = "2",
                description = "Item 2 Description",
                total = 15,
                available = 7,
                allocationRules = "Rules for Item 2"
            ),
            PpeItemData(
                itemId = "3",
                description = "Item 3 Description",
                total = 20,
                available = 12,
                allocationRules = "Rules for Item 3"
            ),
            PpeItemData(
                itemId = "4",
                description = "Item 4 Description",
                total = 8,
                available = 3,
                allocationRules = "Rules for Item 4"
            )
        )

    }



    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}
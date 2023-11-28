package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.Dashboard.DashboardActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsAdapter
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsData
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class PpeItemsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , PpeItemAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressbar: ProgressBar
    private lateinit var ppeItemsAdapter: PpeItemAdapter
    private lateinit var ppeItemsList: MutableList<PpeItemData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var recordsReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppe_items)

        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.ppeItem_drawerLayout)
        progressbar = findViewById(R.id.progressBar)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE

//        ppeItemsAdapter = PpeItemAdapter(ppeItemsList,this)

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

        database = FirebaseDatabase.getInstance()
        recordsReference = database.getReference("PpeItems")

        ppeItemsList = mutableListOf()
        ppeItemsAdapter = PpeItemAdapter(ppeItemsList, this)

        recyclerView = findViewById(R.id.ppeItem_RecyclerView)

        recyclerView.adapter = ppeItemsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadPpeItemsFromFirebase()

//       recyclerView = findViewById(R.id.ppeItem_RecyclerView)
//
//
//        recyclerView.adapter = PpeItemAdapter(ppeItemsList, this)
//        recyclerView.layoutManager = LinearLayoutManager(this)
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
                val intent = Intent(this, RecordsActivity::class.java)
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
    private fun loadPpeItemsFromFirebase() {
        progressbar.visibility = View.VISIBLE

        val databaseReference = FirebaseDatabase.getInstance().reference.child("PpeItems")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newPpeItemsList = mutableListOf<PpeItemData>()
                Log.d("PPEItems", "onDataChange called")
                for (ppeItemSnapshot in dataSnapshot.children) {
                    Log.d("PPEItems", "ItemName: ${ppeItemSnapshot.child("itemName").getValue(String::class.java)}")
                    val itemId = ppeItemSnapshot.child("itemId").getValue(String::class.java)
                    val itemDescription = ppeItemSnapshot.child("itemDescription").getValue(String::class.java)
                    val total = ppeItemSnapshot.child("total").getValue(Int::class.java)
                    val available = ppeItemSnapshot.child("available").getValue(Int::class.java)

                    if ( itemId != null && itemDescription != null && total != null && available != null) {
                        val ppeItemData = PpeItemData(itemId,itemDescription, total, available)
                        newPpeItemsList.add(ppeItemData)
                    }
                }

                // Directly manipulate the list in the activity
                ppeItemsList.clear()
                ppeItemsList.addAll(newPpeItemsList)

                if (newPpeItemsList.isEmpty()) {
                    // Handle empty list scenario if needed
                }

                // Notify the adapter that the data set has changed
                ppeItemsAdapter.notifyDataSetChanged()

                progressbar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                progressbar.visibility = View.GONE
                Log.e("PPEItems", "onCancelled: ${databaseError.message}")
            }
        })
    }
    override fun onItemClick(position: Int) {
    }
}
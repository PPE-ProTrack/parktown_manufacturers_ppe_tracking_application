package com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords

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
import com.example.parktown_manufacturers_ppe_tracking_application.Dashboard.DashboardActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.RecordIssuanceActivity
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Profile.ProfileActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RecordsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
    , RecordsAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressbar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var recordsReference: DatabaseReference
    private lateinit var recordsList: MutableList<RecordsData>
    private lateinit var recordsAdapter: RecordsAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        auth = Firebase.auth
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.records_drawerLayout)
        progressbar = findViewById(R.id.progressBar)
        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE

        //get current user
        val currentUser = auth.currentUser


        toolbar.setTitle("Issuance")
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
        navigationView.setCheckedItem(R.id.nav_issuance)

        database = FirebaseDatabase.getInstance()
        recordsReference = database.getReference("issuance_records")

        recordsList = mutableListOf()
        recordsAdapter = RecordsAdapter(recordsList, this)

        recyclerView = findViewById(R.id.record_RecyclerView)

        recyclerView.adapter = recordsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchDataFromFirebase()
        progressbar.visibility = View.GONE


        val fabAddObservation: FloatingActionButton = findViewById(R.id.recordIssuance_FloatingActionButton)
        fabAddObservation.setOnClickListener {
            // Handle the click event for adding new observations
            startActivity(Intent(this, RecordIssuanceActivity::class.java))
        }
    }

    private fun fetchDataFromFirebase() {
        progressbar.visibility = View.VISIBLE

        // Clear existing data in the list before fetching new data
        recordsList.clear()

        recordsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val record = dataSnapshot.getValue(RecordsData::class.java)
                    record?.let { recordsList.add(it) }
                }

                // Notify the adapter that the data set has changed
                recordsAdapter.notifyDataSetChanged()

                progressbar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                progressbar.visibility = View.GONE
            }
        })
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
                val intent = Intent(this, EmployeeActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_issuance -> {
//
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemClick(position: Int) {
    }
}
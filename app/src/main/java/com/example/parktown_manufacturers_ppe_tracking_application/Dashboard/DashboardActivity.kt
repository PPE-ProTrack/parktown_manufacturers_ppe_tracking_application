package com.example.parktown_manufacturers_ppe_tracking_application.Dashboard


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
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsAdapter
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsData
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.RecordIssuanceActivity
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemAdapter
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemData
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Profile.ProfileActivity

import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase



class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, PpeItemAdapter.OnItemClickListener {

    private lateinit var toolbar : Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var availableProgressbar: ProgressBar
    private lateinit var createIssuanceButton: Button
    private lateinit var availableSeeMore: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var recordsReference: DatabaseReference
    private lateinit var ppeItemList: MutableList<PpeItemData>
    private lateinit var ppeItemAdapter: PpeItemAdapter
    private lateinit var avail_items_TextView: TextView

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = Firebase.auth
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.dashboard_drawerLayout)
        availableProgressbar = findViewById(R.id.available_progressBar)
        availableProgressbar.bringToFront()
        //progressbar.visibility = View.VISIBLE
        createIssuanceButton = findViewById(R.id.createIssuanceButton)
        availableSeeMore = findViewById(R.id.availableSeeMore_Button)
        avail_items_TextView = findViewById(R.id.avail_items_TextView)


        //get current user
        val currentUser = auth.currentUser

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
        navigationView.setCheckedItem(R.id.nav_dashboard)

//        val AvailablerecyclerView: RecyclerView = findViewById(R.id.dashboard_available_RecyclerView)

        createIssuanceButton.setOnClickListener {

            val intent = Intent(this, RecordIssuanceActivity::class.java)
            startActivity(intent)
        }


        database = FirebaseDatabase.getInstance()
        recordsReference = database.getReference("PpeItems")

        ppeItemList = mutableListOf()
        ppeItemAdapter = PpeItemAdapter(ppeItemList, this)

        recyclerView = findViewById(R.id.dashboard_available_RecyclerView)

        recyclerView.adapter = ppeItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadPpeItemsFromFirebase()
//        availableProgressbar.visibility = View.GONE

        availableSeeMore.setOnClickListener {

            val intent = Intent(this, RecordsActivity::class.java)
            startActivity(intent)
        }
        //progressbar.visibility = View.GONE

    }
    private fun loadPpeItemsFromFirebase() {
        availableProgressbar.visibility = View.VISIBLE

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
                ppeItemList.clear()
                ppeItemList.addAll(newPpeItemsList)


                //                avail_items_TextView.setText("Items: $ppeItemList.size")
                avail_items_TextView.text = "Items: ${ppeItemList.size}"
                if (newPpeItemsList.isEmpty()) {
                    // Handle empty list scenario if needed
                }

                // Notify the adapter that the data set has changed
                ppeItemAdapter.notifyDataSetChanged()

                availableProgressbar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                availableProgressbar.visibility = View.GONE
                Log.e("PPEItems", "onCancelled: ${databaseError.message}")
            }
        })
    }
//    object DashboardPendingManager{
//        val DashboardPendingList: MutableList<DashboardPendingData> = mutableListOf(
//            DashboardPendingData(
//                empName = "Ndumiso Zwane",
//                itemDescription = "Skull cap",
//                issuanceDate = "24 April 2023",
//            ),
//            DashboardPendingData(
//                empName = "Uzair Cotwall",
//                itemDescription = "Overralls",
//                issuanceDate = "20 April 2023",
//            ),
//            DashboardPendingData(
//
//                empName = "Halalisani Mdlalose",
//                itemDescription = "Helmet",
//                issuanceDate = "15 April 2023",
//            ),
//            DashboardPendingData(
//                empName = "Mufhatutshedzwa Todani",
//                itemDescription = "Skull Cap",
//                issuanceDate = "12 April 2023",
//            ),
//            DashboardPendingData(
//                empName = "Mufhatutshedzwa Todani",
//                itemDescription = "Skull Cap",
//                issuanceDate = "12 April 2023",
//            ),
//            DashboardPendingData(
//                empName = "Mufhatutshedzwa Todani",
//                itemDescription = "Skull Cap",
//                issuanceDate = "12 April 2023",
//            ),
//            DashboardPendingData(
//                empName = "Mufhatutshedzwa Todani",
//                itemDescription = "Skull Cap",
//                issuanceDate = "12 April 2023",
//            ),
//        )
//
//    }
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
                val intent = Intent(this, RecordsActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onItemClick(position: Int) {

    }

}
package com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference

class DepartmentDetailsActivity : AppCompatActivity() {
    private lateinit var backButton: Button

    private lateinit var toolbar : Toolbar
    private lateinit var progressbar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    // Reference to the Firebase Realtime Database
    private lateinit var databaseReference: DatabaseReference

    private lateinit var emptyMessage: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department_details)

        toolbar = findViewById(R.id.toolbar)

        progressbar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.dept_emp_RecyclerView)

        progressbar.bringToFront()
        progressbar.visibility = View.VISIBLE
        emptyMessage = findViewById(R.id.empty_message)

        backButton = findViewById(R.id.department_details_backbutton)

        backButton.setOnClickListener {
            val intent = Intent(this, DepartmentActivity::class.java)
            startActivity(intent)
        }
    }
}
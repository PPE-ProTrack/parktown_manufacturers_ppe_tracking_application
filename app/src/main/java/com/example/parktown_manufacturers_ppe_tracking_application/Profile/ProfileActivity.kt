package com.example.parktown_manufacturers_ppe_tracking_application.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.parktown_manufacturers_ppe_tracking_application.Dashboard.DashboardActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Department.DepartmentActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister.SignInActivity
import com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister.SignUpActivity
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var save: Button
    private lateinit var logout: Button
    private lateinit var changeButton: Button
    private lateinit var create_account: Button
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var password: EditText
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView : NavigationView
    private lateinit var toolbar : Toolbar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initializing components
        save = findViewById(R.id.save_profile)
        create_account = findViewById(R.id.add_admin_button)
        logout = findViewById(R.id.logout_profile)
        changeButton = findViewById(R.id.change_password_button)
        name = findViewById(R.id.profile_name)
        email = findViewById(R.id.profile_email)
        password = findViewById(R.id.profile_password)
        auth = Firebase.auth
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.profile_drawerLayout)
        toolbar = findViewById(R.id.toolbar)

        //get current user
        val currentUser = auth.currentUser

        //Open register screen
        create_account.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        // Disable password EditText by default
        password.isEnabled = false

        changeButton.setOnClickListener {
            // Enable password EditText for editing
            password.isEnabled = true
        }
        //Logout button
        logout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        save.setOnClickListener {
            // Get the new password from the password EditText
            val newPassword = password.text.toString()
            val numberRegex = Regex(".*\\d.*")
            val specialCharRegex = Regex(".*[@#\$!].*")
            val capitalLetterregex = Regex("[A-Z]")

            if (!newPassword.matches(numberRegex)) {
                password.error = "Password must contain at least one number"
                password.requestFocus()
                return@setOnClickListener
            }
            if (!newPassword.matches(specialCharRegex)) {
                password.error = "Password must contain at least one of @, #, $, !"
                password.requestFocus()
                return@setOnClickListener
            }
            if (!capitalLetterregex.containsMatchIn(newPassword)) {
                password.error = "Password must contain at least one capital letter."
                password.requestFocus()
                return@setOnClickListener
            }
            if (newPassword.length !in 8..15) {
                password.error = "Password must be 8-15 characters long"
                password.requestFocus()
                return@setOnClickListener
            }

            // Update the user's password
            auth.currentUser?.updatePassword(newPassword)

            // Disable password EditText after saving
            password.isEnabled = false

            // Informing the user that the password has been saved
            Toast.makeText(this, "Password saved successfully", Toast.LENGTH_SHORT).show()
        }

        currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get the updated user
                val updatedUser = FirebaseAuth.getInstance().currentUser
                name.text = updatedUser?.displayName
                email.text = updatedUser?.email
            } else {
                Toast.makeText(this, "Could not retrieve user details", Toast.LENGTH_SHORT).show()
            }
        }

        toolbar.setTitle("Profile")

        setSupportActionBar(toolbar)

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black))
        toggle.getDrawerArrowDrawable().barLength = 100.0F
        toggle.getDrawerArrowDrawable().barThickness = 10.0F
        toggle.getDrawerArrowDrawable().gapSize = 20.0F
        toggle.syncState()


        navigationView.setNavigationItemSelectedListener(this)
        val headerView = navigationView.getHeaderView(0)

        // Find the TextView within the header view
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
}
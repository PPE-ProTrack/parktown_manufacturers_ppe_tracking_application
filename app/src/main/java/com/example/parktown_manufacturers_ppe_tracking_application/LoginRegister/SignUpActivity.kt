package com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var request_btn: Button
    private lateinit var login: Button
    lateinit var name: EditText
    lateinit var surname: EditText
    lateinit var workEmail: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var auth: FirebaseAuth
    lateinit var progressbar: ProgressBar

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        request_btn = findViewById(R.id.request_button)
        login = findViewById(R.id.already_have_an_account_Button)
        name = findViewById(R.id.firstname_edittext)
        surname = findViewById(R.id.lastname_edittext)
        workEmail = findViewById(R.id.work_email_editext)
        confirmPassword = findViewById(R.id.confirmpassword_edittext)

        password = findViewById(R.id.password_edittext)
        progressbar = findViewById(R.id.signup_progressBar)

        //Open Login screen if you have an account
        login.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        //register new user
        request_btn.setOnClickListener {

            RegisterUser()

        }
    }
    fun RegisterUser(){

        val email = workEmail.text.toString()
        val name = name.text.toString()
        val surname = surname.text.toString()
        val password = password.text.toString()
        val confirmPassword = confirmPassword.text.toString()

        progressbar.visibility = View.VISIBLE
        login.visibility = View.INVISIBLE


        validateInput()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility = View.GONE
                login.visibility = View.VISIBLE
                if (task.isSuccessful) {

                    // Registration was successful.
                    val user = auth.currentUser
                    val userId = user?.uid

                    // Set display name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName("$name $surname")
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {

                                // Store additional user information in Firebase Database
                                if (userId != null) {
                                    val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
                                    userRef.child("name").setValue(name)
                                    userRef.child("surname").setValue(surname)
                                    userRef.child("email").setValue(email)

                                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
                                }
                            }}

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }

    }
    // Add this validateInput method
    fun validateInput(): Boolean {
        val email = workEmail.text.toString()
        val name = name.text.toString()
        val surname = surname.text.toString()
        val password = password.text.toString()
        val confirmPassword = confirmPassword.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            login.visibility = View.VISIBLE
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password and confirm password do not match.", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            login.visibility = View.VISIBLE
            return false
        }

        // Add more validation logic here if needed

        return true
    }
}
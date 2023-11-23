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
import com.example.parktown_manufacturers_ppe_tracking_application.Dashboard.DashboardActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var create_account: Button
    private lateinit var login_button: Button
    lateinit var login_email: EditText
    lateinit var login_password: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        create_account = findViewById(R.id.dont_have_an_account_Button)
        login_email = findViewById(R.id.email_editext)
        login_password = findViewById(R.id.password_edittext)
        login_button = findViewById(R.id.login_button)
        progressbar = findViewById(R.id.login_progressBar)

        //Open register screen
        create_account.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //verify user details
        login_button.setOnClickListener {
            SignInUser()

        }
    }

    fun SignInUser(){
        val email = login_email.text.toString()
        val password = login_password.text.toString()

        progressbar.visibility = View.VISIBLE
        create_account.visibility = View.INVISIBLE

        validateInput()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility = View.GONE
                create_account.visibility = View.VISIBLE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

    fun validateInput(): Boolean {
        val email = login_email.text.toString()
        val password = login_password.text.toString()


        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your Email.", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            create_account.visibility = View.VISIBLE
            return false;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your Password.", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            create_account.visibility = View.VISIBLE
            return false;
        }

        return true
    }
}
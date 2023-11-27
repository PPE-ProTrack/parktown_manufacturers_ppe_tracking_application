package com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.*
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
    private lateinit var showPasswordButton: ImageButton
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        create_account = findViewById(R.id.dont_have_an_account_Button)
        login_email = findViewById(R.id.email_editext)
        login_password = findViewById(R.id.password_edittext)
        login_button = findViewById(R.id.login_button)
        progressbar = findViewById(R.id.login_progressBar)
        showPasswordButton = findViewById(R.id.show_password_button)

        // Toggle password visibility
        showPasswordButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                login_password.transformationMethod = null
                showPasswordButton.setImageResource(R.drawable.hide1)
            } else {
                login_password.transformationMethod = android.text.method.PasswordTransformationMethod()
                showPasswordButton.setImageResource(R.drawable.view1)
            }
            login_password.setSelection(login_password.text.length)
        }
        //Open register screen
        create_account.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //verify user details
        login_button.setOnClickListener {
            val userEmail = login_email.text.toString()
            val userPassword = login_password.text.toString()
            val numberRegex = Regex(".*\\d.*")
            val specialCharRegex = Regex(".*[@#\$!].*")
            val capitalLetterregex = Regex("[A-Z]")


            progressbar.visibility = View.VISIBLE
            create_account.visibility = View.INVISIBLE


            if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                login_email.error = "Please enter a valid email address"
                login_email.requestFocus()
                progressbar.visibility = View.GONE
                create_account.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (!userPassword.matches(numberRegex)) {
                login_password.error = "Password must contain at least one number"
                login_password.requestFocus()
                progressbar.visibility = View.GONE
                create_account.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (!userPassword.matches(specialCharRegex)) {
                login_password.error = "Password must contain at least one of @, #, $, !"
                login_password.requestFocus()
                progressbar.visibility = View.GONE
                create_account.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (!capitalLetterregex.containsMatchIn(userPassword)) {
                login_password.error = "Password must contain at least one capital letter."
                login_password.requestFocus()
                progressbar.visibility = View.GONE
                create_account.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (userPassword.length !in 8..15) {
                login_password.error = "Password must be 8-15 characters long"
                login_password.requestFocus()
                progressbar.visibility = View.GONE
                create_account.visibility = View.VISIBLE
                return@setOnClickListener
            }

            SignInUser()

        }
    }

    fun SignInUser(){
        val email = login_email.text.toString()
        val password = login_password.text.toString()

        progressbar.visibility = View.VISIBLE
        create_account.visibility = View.INVISIBLE

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


}
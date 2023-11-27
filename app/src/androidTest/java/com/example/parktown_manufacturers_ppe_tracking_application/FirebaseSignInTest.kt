package com.example.parktown_manufacturers_ppe_tracking_application

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister.SignInActivity
import com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister.SignUpActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith



import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

@RunWith(AndroidJUnit4::class)
class FirebaseSignInTest {

        private lateinit var signInScenario: ActivityScenario<SignInActivity>
        @Before
        fun setup() {

            // Initialize Firebase for testing
            FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())

            // Launch the activity using ActivityScenario
            signInScenario = ActivityScenario.launch(SignInActivity::class.java)
            signInScenario.moveToState(Lifecycle.State.CREATED)


        }


        @Test
        fun testLoginUser() {
            signInScenario.onActivity { activity ->
                // Set up test data
                activity.login_email.setText("test3@example.com")
                activity.login_password.setText("password")

                // Simulate user login
                activity.SignInUser()

                // Check if the user login was successful
                val user = FirebaseAuth.getInstance().currentUser

                Assert.assertNotNull("User should be logged in successfully", user)

                // Optionally, you can check user properties or do more specific verifications
                // Example: Verify that the user's email matches the provided email
                //assertEquals("test@example.com", user?.email)
            }
        }

    }

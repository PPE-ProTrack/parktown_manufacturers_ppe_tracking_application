package com.example.parktown_manufacturers_ppe_tracking_application



import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister.SignInActivity
import com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister.SignUpActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseSignUpTest {

    private lateinit var signUpScenario: ActivityScenario<SignUpActivity>


    @Before
    fun setup() {

        // Launch the activity using ActivityScenario
        signUpScenario = ActivityScenario.launch(SignUpActivity::class.java)

        // Initialize Firebase for testing
        signUpScenario.onActivity { activity ->
            FirebaseApp.initializeApp(activity)
        }
    }

    @Test
    fun testRegisterUser() {
        signUpScenario.onActivity { activity ->
            // Set up test data
            activity.workEmail.setText("test5@gmail.com")
            activity.name.setText("Joe")
            activity.surname.setText("Doe")
            activity.password.setText("Ndum@999")
            activity.confirmPassword.setText("Ndum@999")

            // Simulate user registration
            activity.RegisterUser()

            // Check if the user registration was successful
            val user = FirebaseAuth.getInstance().currentUser

            assertNotNull("User should be registered successfully", user)

            // Optionally, you can check user properties or do more specific verifications
            // Example: Verify that the user's email matches the provided email
            assertEquals("test5@gmail.com", user?.email)
        }
    }
}
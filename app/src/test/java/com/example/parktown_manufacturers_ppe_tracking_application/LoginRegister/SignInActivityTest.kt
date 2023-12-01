package com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister


import android.content.Context
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import androidx.test.core.app.ApplicationProvider

//the code was taken from Developers
//Link: https://developer.android.com/training/testing/local-tests
//Accessed 27 November 2023
@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class SignInActivityTest {

    private lateinit var signInActivity: SignInActivity
    // Mock class to represent EditText
    private class MockEditText(context: Context) : android.widget.EditText(context)
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        signInActivity = SignInActivity()

        // Initialize properties using reflection
        val emailField = SignInActivity::class.java.getDeclaredField("login_email")
        emailField.isAccessible = true
        emailField.set(signInActivity, MockEditText(context))

        val passwordField = SignInActivity::class.java.getDeclaredField("login_password")
        passwordField.isAccessible = true
        passwordField.set(signInActivity, MockEditText(context))
    }

    @Test
    fun `invalid email should return false`() {
        val userEmail = "invalidEmail"
        assertFalse(signInActivity.validateSignInInput(
            userEmail,  "StrongPassword123!@#"
        ))
    }

    @Test
    fun `password without capital letter should return false`() {
        val userPassword = "passwordwithoutcapital"
        assertFalse(signInActivity.validateSignInInput(
            "john.doe@example.com",  userPassword
        ))
    }

    @Test
    fun `password without number should return false`() {
        val userPassword = "PasswordWithoutNumber"
        assertFalse(signInActivity.validateSignInInput(
            "john.doe@example.com",  userPassword
        ))
    }

    @Test
    fun `password without special character should return false`() {
        val userPassword = "PasswordWithoutSpecial"
        assertFalse(signInActivity.validateSignInInput(
            "john.doe@example.com",  userPassword
        ))
    }

    @Test
    fun `password length less than 8 characters should return false`() {
        val userPassword = "Short1!"
        assertFalse(signInActivity.validateSignInInput(
            "john.doe@example.com",  userPassword
        ))
    }

    @Test
    fun `password length more than 15 characters should return false`() {
        val userPassword = "TooLongPassword123!@#$"
        assertFalse(signInActivity.validateSignInInput(
            "john.doe@example.com",  userPassword
        ))
    }


    @Test
    fun `valid inputs should return true`() {
        val userEmail = "john.doe@example.com"
        val userPassword = "StrongPass@1"

        assertTrue(SignInActivity().validateSignInInput(
            userEmail,userPassword
        ))
    }

}
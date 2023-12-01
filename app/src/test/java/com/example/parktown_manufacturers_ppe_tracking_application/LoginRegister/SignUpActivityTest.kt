package com.example.parktown_manufacturers_ppe_tracking_application.LoginRegister


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

//the code was taken from Developers
//Link: https://developer.android.com/training/testing/local-tests
//Accessed 27 November 2023
@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class SignUpActivityTest {
    private lateinit var signUpActivity: SignUpActivity
    // Mock class to represent EditText
    private class MockEditText(context: Context) : android.widget.EditText(context)
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        signUpActivity = SignUpActivity()

        // Initialize properties using reflection
        val emailField = SignUpActivity::class.java.getDeclaredField("workEmail")
        emailField.isAccessible = true
        emailField.set(signUpActivity, MockEditText(context))

        val passwordField = SignUpActivity::class.java.getDeclaredField("password")
        passwordField.isAccessible = true
        passwordField.set(signUpActivity, MockEditText(context))

        val confirmPasswordField = SignUpActivity::class.java.getDeclaredField("confirmPassword")
        confirmPasswordField.isAccessible = true
        confirmPasswordField.set(signUpActivity, MockEditText(context))

        val nameField = SignUpActivity::class.java.getDeclaredField("name")
        nameField.isAccessible = true
        nameField.set(signUpActivity, MockEditText(context))

        val surnameField = SignUpActivity::class.java.getDeclaredField("surname")
        surnameField.isAccessible = true
        surnameField.set(signUpActivity, MockEditText(context))
    }
    @Test
    fun `empty surname field should return false`() {
        val userSurname = ""
        assertFalse(signUpActivity.validateInput(
            "john.doe@example.com", "John", userSurname,
            "StrongPassword123!@#", "StrongPassword123!@#"
        ))
    }

    @Test
    fun `invalid email should return false`() {
        val userEmail = "invalidEmail"
        assertFalse(signUpActivity.validateInput(
            userEmail, "John", "Doe",
            "StrongPassword123!@#", "StrongPassword123!@#"
        ))
    }

    @Test
    fun `password without capital letter should return false`() {
        val userPassword = "passwordwithoutcapital"
        assertFalse(signUpActivity.validateInput(
            "john.doe@example.com", "John", "Doe",
            userPassword, userPassword
        ))
    }

    @Test
    fun `password without number should return false`() {
        val userPassword = "PasswordWithoutNumber"
        assertFalse(signUpActivity.validateInput(
            "john.doe@example.com", "John", "Doe",
            userPassword, userPassword
        ))
    }

    @Test
    fun `password without special character should return false`() {
        val userPassword = "PasswordWithoutSpecial"
        assertFalse(signUpActivity.validateInput(
            "john.doe@example.com", "John", "Doe",
            userPassword, userPassword
        ))
    }

    @Test
    fun `password length less than 8 characters should return false`() {
        val userPassword = "Short1!"
        assertFalse(signUpActivity.validateInput(
            "john.doe@example.com", "John", "Doe",
            userPassword, userPassword
        ))
    }

    @Test
    fun `password length more than 15 characters should return false`() {
        val userPassword = "TooLongPassword123!@#$"
        assertFalse(signUpActivity.validateInput(
            "john.doe@example.com", "John", "Doe",
            userPassword, userPassword
        ))
    }

    @Test
    fun `mismatched passwords should return false`() {
        val userPassword = "Password123!@#"
        val userConfirmPassword = "DifferentPassword123!@#"
        assertFalse(signUpActivity.validateInput(
            "john.doe@example.com", "John", "Doe",
            userPassword, userConfirmPassword
        ))
    }

    @Test
    fun `valid inputs should return true`() {
        val userName = "John"
        val userSurname = "Doe"
        val userEmail = "john.doe@example.com"
        val userPassword = "StrongPass@12"
        val userConfirmPassword = "StrongPass@12"

        assertTrue(signUpActivity.validateInput(
            userEmail, userName, userSurname,
            userPassword, userConfirmPassword
        ))

    }



}





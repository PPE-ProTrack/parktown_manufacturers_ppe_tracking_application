package com.example.parktown_manufacturers_ppe_tracking_application

import com.example.parktown_manufacturers_ppe_tracking_application.Employee.AddEmployeeActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import org.hamcrest.Matchers.not


@RunWith(AndroidJUnit4::class)
class AddEmployeeActivityAndroidTest {

        private lateinit var activityScenario: ActivityScenario<AddEmployeeActivity>

        @Before
        fun setUp() {
            // Launch the activity using ActivityScenario
            activityScenario = ActivityScenario.launch(AddEmployeeActivity::class.java)

        }

        @Test
        fun testSaveBtn() {

            onView(withId(R.id.empName_EditText)).perform(typeText("John"), closeSoftKeyboard())
            onView(withId(R.id.empSurname_EditText)).perform(typeText("Doe"), closeSoftKeyboard())
            Thread.sleep(4000)

            // Click the save button
            onView(withId(R.id.btnSave)).perform(click())
        }
    }








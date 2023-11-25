package com.example.parktown_manufacturers_ppe_tracking_application.Employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var empIdEditText : EditText
    private lateinit var empNameEditText : EditText
    private lateinit var empSurnameEditText : EditText
    private lateinit var empDepartmentIdEditText : EditText
    private lateinit var empDepartmentNameEditText : EditText
    lateinit var btnSave: Button
    private lateinit var toolbar : Toolbar
   // private lateinit var progressbar: ProgressBar

    // Reference to the Firebase Realtime Database
    private lateinit var databaseReference: DatabaseReference

    lateinit var backButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        toolbar = findViewById(R.id.toolbar)

       // progressbar = findViewById(R.id.progressBar)
       // progressbar.bringToFront()
        //progressbar.visibility = View.VISIBLE

        // Initialize EditTexts
        empIdEditText = findViewById(R.id.empId_EditText)
        empNameEditText = findViewById(R.id.empName_EditText)
        empSurnameEditText = findViewById(R.id.empSurname_EditText)
        empDepartmentIdEditText = findViewById(R.id.empDepartmentId_EditText)
        empDepartmentNameEditText = findViewById(R.id.empDepartmentName_EditText)

        // Initialize Button
        btnSave = findViewById(R.id.btnSave)
        backButton = findViewById(R.id.add_employee_backbutton)

        backButton.setOnClickListener {
            val intent = Intent(this,EmployeeActivity::class.java)
            startActivity(intent)
        }

        toolbar.setTitle("Add Employee")
        toolbar.titleMarginStart= 150
        setSupportActionBar(toolbar)


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("employees")
        empID(databaseReference)


        btnSave.setOnClickListener {

            saveBtn()

        }

    }

    fun saveBtn(){

        // Create an instance of EmployeeData with values from EditTexts
        val employeeData = EmployeeData(
            empIdEditText.text.toString().toInt(),
            empNameEditText.text.toString(),
            empSurnameEditText.text.toString(),
            empDepartmentIdEditText.text.toString().toInt(),
            empDepartmentNameEditText.text.toString()
        )

        // Push the data to Firebase Realtime Database
        val newEmployeeReference = databaseReference.push()
        newEmployeeReference.setValue(employeeData, object : DatabaseReference.CompletionListener {
            override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference) {
                if (databaseError == null) {
                    // Data saved successfully
                    // You can add any logic here that should execute after the data is saved
                } else {
                    // Handle the error
                    // You can log the error or show a message to the user
                }
            }
        })
    }

    fun empID(databaseReference : DatabaseReference ){
        // Query the database to find the highest existing employee ID
        databaseReference.orderByChild("employeeId").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (employeeSnapshot in dataSnapshot.children) {
                            val highestEmployeeId = employeeSnapshot.child("employeeId").getValue(Int::class.java)
                            // Increment the highest employee ID by 1 for the next available ID
                            val nextEmployeeId = highestEmployeeId?.plus(1) ?: 1001
                            empIdEditText.setText(nextEmployeeId.toString())
                        }
                    } else {
                        // If no employee exists, start from 1001
                        empIdEditText.setText("1001")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                    // You can log the error or show a message to the user
                }
            })
    }
}
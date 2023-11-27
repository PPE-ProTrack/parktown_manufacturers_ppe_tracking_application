package com.example.parktown_manufacturers_ppe_tracking_application.Employee

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*


class AddEmployeeActivity : AppCompatActivity() {

     lateinit var empIdEditText : EditText
     lateinit var empNameEditText : EditText
     lateinit var empSurnameEditText : EditText

     var empDepartmentId : String = ""
     var empDepartmentName : String = ""
    lateinit var btnSave: Button
    private lateinit var toolbar : Toolbar
    lateinit var empDepartmentNameSpinner: Spinner

    var empID = 0
    var employeeName = ""
    var employeeSurname = ""
    var fullName = "$employeeName $employeeSurname"
    var departmentId =  0
    var departmentName = ""

   // private lateinit var progressbar: ProgressBar

    // Reference to the Firebase Realtime Database
    lateinit var employeesDatabaseReference: DatabaseReference

    lateinit var departmentsDatabaseReference: DatabaseReference

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
        empDepartmentNameSpinner = findViewById(R.id.departmentName_Spinner)

        // Set a selection listener for the spinner
        empDepartmentNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDepartmentName = parent?.getItemAtPosition(position).toString()
                updateDepartmentFields(selectedDepartmentName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

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
        employeesDatabaseReference = FirebaseDatabase.getInstance().reference.child("employees")
        empID(employeesDatabaseReference)


        btnSave.setOnClickListener {
            saveBtn()
        }

        // Initialize Firebase Database reference
        departmentsDatabaseReference = FirebaseDatabase.getInstance().reference.child("departments")

        // Fetch department data from Firebase and populate the spinner
        fetchDepartments()

    }

//    fun saveBtn(){
//
//        // Create an instance of EmployeeData with values from EditTexts
//        val employeeData = EmployeeData(
//            empIdEditText.text.toString().toInt(),
//            empNameEditText.text.toString(),
//            empSurnameEditText.text.toString(),
//            empNameEditText.text.toString() + " " + empSurnameEditText.text.toString(),
//            empDepartmentIdEditText.text.toString().toInt(),
//            empDepartmentNameEditText.text.toString()
//        )
//
//        // Push the data to Firebase Realtime Database
//        val newEmployeeReference = employeesDatabaseReference.push()
//        newEmployeeReference.setValue(employeeData, object : DatabaseReference.CompletionListener {
//            override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference) {
//                if (databaseError == null) {
//                    // Data saved successfully
//                    // You can add any logic here that should execute after the data is saved
//                } else {
//                    // Handle the error
//                    // You can log the error or show a message to the user
//                }
//            }
//        })
//    }
    private fun updateDepartmentFields(selectedDepartmentName: String) {
        // Fetch departmentId using the selected departmentName
        departmentsDatabaseReference.orderByChild("departmentName").equalTo(selectedDepartmentName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (departmentSnapshot in dataSnapshot.children) {
                            val departmentId = departmentSnapshot.child("departmentId").getValue(Int::class.java)
                            updateEmployeeFields(departmentId, selectedDepartmentName)
                        }
                    } else {
                        showToast("Error fetching department information")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showToast("Error fetching department information")
                }
            })
    }

    private fun updateEmployeeFields(departmentId: Int?, departmentName: String) {
        // Update empDepartmentId and empDepartmentName
        if (departmentId != null) {
            empDepartmentId = departmentId.toString()
        }

        empDepartmentName = departmentName
    }

     fun saveBtn() {
         empID = empIdEditText.text.toString().toInt()
         employeeName = empNameEditText.text.toString().trim()
         employeeSurname = empSurnameEditText.text.toString().trim()
         fullName = "$employeeName $employeeSurname"
         departmentId =  empDepartmentId.toInt()
         departmentName = empDepartmentName

        employeesDatabaseReference.orderByChild("employeeFullName").equalTo(fullName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Employee with the same name and surname exists
                        val count = dataSnapshot.childrenCount.toInt() + 1
                        val updatedFullName = "$fullName $count"

                        // Create an instance of EmployeeData with updated full name
                        val employeeData = EmployeeData(
                            empID,
                            employeeName,
                            employeeSurname,
                            updatedFullName,
                            departmentId,
                            departmentName
                        )

                        // Push the data to Firebase Realtime Database
                            val newEmployeeReference = employeesDatabaseReference.push()
                        employeesDatabaseReference.child(empID.toString()).setValue(employeeData, object : DatabaseReference.CompletionListener {
                            override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference) {
                                if (databaseError == null) {
                                    showToast("Employee added successfully")
                                    // Update totalEmployees in the department table
                                    updateTotalEmployees()
                                    finish() // Close the activity after successful addition
                                } else {
                                    showToast("Failed to add employee")
                                }
                            }
                        })
                    } else {
                        // No employee with the same name and surname exists
                        // Continue with the regular save logic
                        //val totalEmployees = 0 // Initialize totalEmployees to zero

                        val employeeData = EmployeeData(
                            empID,
                            employeeName,
                            employeeSurname,
                            fullName,
                            departmentId,
                            departmentName
                        )

                        // Push the data to Firebase Realtime Database
                            //val newEmployeeReference = employeesDatabaseReference.push()
                        employeesDatabaseReference.child(empID.toString()).setValue(employeeData, object : DatabaseReference.CompletionListener {
                            override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference) {
                                if (databaseError == null) {
                                    showToast("Employee added successfully")
                                    // Update totalEmployees in the department table
                                    updateTotalEmployees()
                                    finish() // Close the activity after successful addition
                                } else {
                                    showToast("Failed to add employee")
                                }
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showToast("Error checking for existing employees")
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

    private fun fetchDepartments() {

        val departmentList = mutableListOf<String>()

        // Attach a listener to read the data at the 'departments' path
        departmentsDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    // Assuming each department is stored as a String in the database
                    val departmentName = childSnapshot.child("departmentName").getValue(String::class.java)

                    if (departmentName != null) {
                        departmentList.add(departmentName)
                    }
                }

                // Create an ArrayAdapter using the fetched department data
                val adapter = ArrayAdapter(
                    this@AddEmployeeActivity,
                    android.R.layout.simple_spinner_item,
                    departmentList
                )

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Apply the adapter to the spinner
                empDepartmentNameSpinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    fun updateTotalEmployees() {
        // Increment totalEmployees in the department table
        departmentsDatabaseReference.child(empDepartmentId).child("totalEmployees").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentTotalEmployees = dataSnapshot.getValue(Int::class.java) ?: 0
                val newTotalEmployees = currentTotalEmployees + 1

                // Update totalEmployees in the department table
                departmentsDatabaseReference.child(empDepartmentId).child("totalEmployees").setValue(newTotalEmployees)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Error updating total employees")
            }
        })
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
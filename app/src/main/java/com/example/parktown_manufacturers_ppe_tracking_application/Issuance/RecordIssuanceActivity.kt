package com.example.parktown_manufacturers_ppe_tracking_application.Issuance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeActivity.EmployeeManager.employeeList
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeData
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.Infringements.InfringementsData
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsData
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemData
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class RecordIssuanceActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var saveButton: Button
    private lateinit var employeeSpinner: Spinner
    private lateinit var itemsSpinner: Spinner
    private lateinit var departmentEditText: EditText
    private lateinit var infringementTextView: TextView

    //    private  var empId_: Int = 0
//    private  var itemId: Int = 0
//    private var itemDescription: String = ""
    private var previousItemReturned: Boolean = true
    private lateinit var currentDateEditText: EditText
    // the paths are not yet finalized e.g. (issuance_records and employee_infringements)
    val recordsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("issuance_records")
    val infringementsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("employee_infringements")

    private var selectedPpeItem: PpeItemData? = null
    private var selectedEmployee: EmployeeData? = null
    var selectedOption: String = ""
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_issuance)
        backButton = findViewById(R.id.add_ppe_backbutton)
        saveButton = findViewById(R.id.btnSave)


        itemsSpinner = findViewById(R.id.itemName_Spinner)
        employeeSpinner = findViewById(R.id.empName_Spinner)
        currentDateEditText = findViewById(R.id.current_date_EditText)
        departmentEditText = findViewById(R.id.ppeItemDepartment_EditText)
        infringementTextView = findViewById(R.id.infringement_TextView)


        database = FirebaseDatabase.getInstance()

        fetchItemsFromDatabase()

        fetchEmployeesFromFirebase()
        setCurrentDate()

        backButton.setOnClickListener {
            val intent = Intent(this, RecordsActivity::class.java)
            startActivity(intent)
        }

        val infringementRadioGroup: RadioGroup = findViewById(R.id.infringement_RadioGroup)
        val yesRadioButton: RadioButton = findViewById(R.id.yes_RadioButton)
        val noRadioButton: RadioButton = findViewById(R.id.no_RadioButton)

        infringementRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.yes_RadioButton -> {
                    // Handle 'Yes' RadioButton click
                    previousItemReturned = true
                     selectedOption = "Yes"
                }
                R.id.no_RadioButton -> {
                    // Handle 'No' RadioButton click
                    previousItemReturned = false
                    selectedOption = "No"
                }
            }
        }

        saveButton.setOnClickListener {

//            // check infringements
//            if(previousItemReturned){
//                //yes it was returned
//
//            }else{
//                //No it was not returned
//                processRecordsAndInfringements( 1001, "Skull Cap")
//            }

            if (!previousItemReturned) {
                // Item was not returned, process records and infringements
                val selectedEmployeeId = selectedEmployee?.employeeId
                val selectedEmployeeName = selectedEmployee!!.employeeName
                val selectedItemName = selectedPpeItem!!.itemDescription

                val itemId = selectedPpeItem!!.itemId
                if (selectedEmployee != null && selectedItemName != null && selectedEmployeeId !=null) {
                    val issuanceDate = currentDateEditText.text.toString()

                    // Create an instance of RecordsData with the provided data
                    val issuanceRecord = RecordsData(
                        recordId = generateUniqueRecordId(),
                        empId = selectedEmployeeId,
                        empName = selectedEmployeeName,
                        itemId =itemId,
                        itemDescription = selectedItemName,
                        issuanceDate = issuanceDate,
                        returned = selectedOption,
                    )

                    // Save the RecordsData object to Firebase
                    saveRecordsDataToFirebase(issuanceRecord)
                    // Update the available quantity in Firebase
                    val newAvailableQuantity = selectedPpeItem?.available?.minus(1) ?: 0
                    updateAvailableQuantityInFirebase(itemId, newAvailableQuantity)

                    // Process records and infringements
//                    processRecordsAndInfringements(selectedEmployeeId, selectedItemName)

                    // Create an instance of InfringementsData and save it to Firebase
                    val newInfringement = InfringementsData(
                        empItemIdentifier = "",  // You need to set this identifier appropriately
                        infringementId = "", // Leave it empty to generate a unique ID
                        empId = selectedEmployeeId,
                        itemId = itemId,
                        itemDescription = selectedItemName,
                        itemNotReturnedCount = 1
                    )

                    // Set the empItemIdentifier based on empId and itemDescription
                    newInfringement.empItemIdentifier = "${selectedEmployeeId}${selectedItemName}"

                    // Save the new infringement to Firebase
                    saveInfringementsDataToFirebase(newInfringement)
                } else {
                    Log.e(
                        "RecordIssuanceActivity",
                        "Error getting selected employee or item information"
                    )
                    // Handle the error appropriately
                }

                val intent = Intent(this, RecordsActivity::class.java)
                startActivity(intent)
            }
            else {
                val selectedEmployeeId = selectedEmployee?.employeeId
                val selectedEmployeeName = selectedEmployee!!.employeeName
                val selectedItemName = selectedPpeItem!!.itemDescription

                val itemId = selectedPpeItem!!.itemId
                if (selectedEmployee != null && selectedItemName != null && selectedEmployeeId !=null) {
                    val issuanceDate = currentDateEditText.text.toString()

                    // Create an instance of RecordsData with the provided data
                    val issuanceRecord = RecordsData(
                        recordId = generateUniqueRecordId(),
                        empId = selectedEmployeeId,
                        empName = selectedEmployeeName,
                        itemId =itemId,
                        itemDescription = selectedItemName,
                        issuanceDate = issuanceDate,
                        returned = selectedOption,
                    )

                    // Save the RecordsData object to Firebase
                    saveRecordsDataToFirebase(issuanceRecord)
                    val newAvailableQuantity = selectedPpeItem?.available?.minus(1) ?: 0
                    updateAvailableQuantityInFirebase(itemId, newAvailableQuantity)

                }
                val intent = Intent(this, RecordsActivity::class.java)
                startActivity(intent)
            }
        }

    }
    private fun updateAvailableQuantityInFirebase(itemId: String, newAvailableQuantity: Int) {
        val dbReference = database.getReference("PpeItems").child(itemId) // Convert itemId to String

        // Update the available quantity
        dbReference.child("available").setValue(newAvailableQuantity)
            .addOnSuccessListener {
                // Additional actions after successful update
                // For example, you might want to update UI, show a toast, etc.
                Toast.makeText(this, "Available quantity updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update available quantity: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    // Function to generate a unique record ID using timestamp
    private fun generateUniqueRecordId(): Int {
        return System.currentTimeMillis().toInt()
    }
    // Function to save the RecordsData object to Firebase
    private fun saveRecordsDataToFirebase(recordsData: RecordsData) {
        // Assuming you have a Firebase reference for the issuance_records path
        val recordsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("issuance_records")

        // Save the RecordsData object directly to the Firebase reference
        recordsReference.push().setValue(recordsData)
            .addOnSuccessListener {
                Log.d("RecordIssuanceActivity", "RecordsData saved successfully")
                // Optionally, you can perform additional actions upon success
            }
            .addOnFailureListener {
                Log.e("RecordIssuanceActivity", "Error saving RecordsData: ${it.message}")
                // Handle the error appropriately
            }
    }

    private fun saveInfringementsDataToFirebase(infringementsData: InfringementsData) {
        // Assuming you have a Firebase reference for the employee_infringements path
        Log.d("RecordIssuanceActivity", "Saving infringements data to Firebase") // Add this line
        val infringementsReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("employee_infringements")

        val empId = infringementsData.empId.toString()
        val itemDescription = infringementsData.itemDescription

        // Create a unique identifier using empId and itemDescription
        val empItemIdentifier = "$empId$itemDescription"
        Log.d("RecordIssuanceActivity", "EmpId: $empId, ItemDescription: $itemDescription") // Add this line

        // Check if an infringement already exists for the given employee and item description
        infringementsReference.orderByChild("empItemIdentifier").equalTo(empItemIdentifier)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d("RecordIssuanceActivity", "Checking for existing infringements with empItemIdentifier: $empItemIdentifier")
                    // Iterate through the data to find if the infringement exists
                    for (snapshot in dataSnapshot.children) {
                        Log.d("RecordIssuanceActivity", "Iterating through dataSnapshot: $snapshot")
                        val existingInfringement = snapshot.getValue(InfringementsData::class.java)
                        existingInfringement?.let {
                            if (it.itemDescription == itemDescription) {
                                updateInfringementCount(it)
                                Log.d("RecordIssuanceActivity", "Infringement count updated for empId: $empId, itemDescription: $itemDescription")
                                return // Labeled return to exit the loop
                            }
                        }
                    }

                    // If no existing infringement found, create a new one
                    Log.d("RecordIssuanceActivity", "No existing infringement found, creating a new one")
                    createNewInfringement(infringementsData, empItemIdentifier)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("RecordIssuanceActivity", "Error querying database: ${databaseError.message}")
                    // Handle errors if needed
                }
            })
    }



    private fun updateInfringementCount(existingInfringement: InfringementsData) {
        existingInfringement.itemNotReturnedCount += 1

        // Update the count in the database
        infringementsReference.orderByChild("empItemIdentifier")
            .equalTo(existingInfringement.empItemIdentifier)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val infringementId = snapshot.key
                        infringementId?.let {
                            Log.d("RecordIssuanceActivity", "Updating count for empItemIdentifier: ${existingInfringement.empItemIdentifier}")
                            Log.d("RecordIssuanceActivity", "Existing count: ${existingInfringement.itemNotReturnedCount}")

                            infringementsReference.child(it).setValue(existingInfringement)
                                .addOnSuccessListener {
                                    Log.d("RecordIssuanceActivity", "Infringement count updated successfully")
                                    // Optionally, you can perform additional actions upon success
                                }
                                .addOnFailureListener {
                                    Log.e("RecordIssuanceActivity", "Error updating infringement count: ${it.message}")
                                    // Handle the error appropriately
                                }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("RecordIssuanceActivity", "Error querying database: ${databaseError.message}")
                    // Handle errors if needed
                }
            })
    }




    private fun createNewInfringement(newInfringement: InfringementsData, empItemIdentifier: String) {
        // Save the new infringement to the database
        val infringementsReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("employee_infringements")

        // Set empItemIdentifier as a child node
        val newInfringementReference = infringementsReference.child(empItemIdentifier)
        newInfringementReference.setValue(newInfringement)
            .addOnSuccessListener {
                Log.d("RecordIssuanceActivity", "New infringement created successfully")
                // Optionally, you can perform additional actions upon success
            }
            .addOnFailureListener {
                Log.e("RecordIssuanceActivity", "Error creating new infringement: ${it.message}")
                // Handle the error appropriately
            }
    }
    private fun setCurrentDate() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val formattedDate = dateFormat.format(currentDate)
        currentDateEditText.setText(formattedDate)
    }

    private fun fetchItemsFromDatabase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("PpeItems")

        // Listen for changes in the data
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val itemsList = mutableListOf<PpeItemData>()

                for (snapshot in dataSnapshot.children) {
                    val itemId = snapshot.child("itemId").getValue(String::class.java)
                    val itemDescription = snapshot.child("itemDescription").getValue(String::class.java)
                    val total = snapshot.child("total").getValue(Int::class.java)
                    val available = snapshot.child("available").getValue(Int::class.java)

                    if (itemId != null && itemDescription != null && total != null && available != null) {
                        val ppeItemData = PpeItemData(itemId, itemDescription, total, available)
                        itemsList.add(ppeItemData)
                    }
                }
                // Populate the Spinner with the fetched items
                populateSpinner(itemsList)
            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }


    private fun fetchEmployeesFromFirebase() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("employees")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val employeesList = mutableListOf<EmployeeData>()

                for (snapshot in dataSnapshot.children) {
                    val employeeData = snapshot.getValue(EmployeeData::class.java)
                    employeeData?.let {
                        employeesList.add(it)
                    }
                }

                // Populate the Spinner with the fetched employees
                populateEmployeeSpinner(employeesList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }

    private fun populateEmployeeSpinner(employeesList: List<EmployeeData>) {
        val employeeNames = mutableListOf<String>()

        for (employee in employeesList) {
            employeeNames.add("${employee.employeeFullName}")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, employeeNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        employeeSpinner.adapter = adapter

        // Set a listener for employee selection
        employeeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Capture the entire EmployeeData object
                selectedEmployee = employeesList[position]

                // Now you can use the attributes of selectedEmployee as needed
                val employeeId = selectedEmployee?.employeeId
                val employeeName = selectedEmployee?.employeeName
                val employeeSurname = selectedEmployee?.employeeSurname
                departmentEditText.setText(selectedEmployee?.departmentName)
                // Use the attributes as needed
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case where nothing is selected
                selectedEmployee = null
                departmentEditText.text = null
            }
        }
    }

    private fun populateSpinner(itemsList: List<PpeItemData>) {
        // Assuming you have a spinner named "itemSpinner" in your layout
        val itemNames = mutableListOf<String>()

        for (item in itemsList) {
            itemNames.add(item.itemDescription)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        itemsSpinner.adapter = adapter

        // Set a listener for item selection
        itemsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Store the selected item information
                selectedPpeItem = itemsList[position]
                infringementTextView.text = "Was the returned the ${selectedPpeItem!!.itemDescription} last time?"
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case where nothing is selected
                selectedPpeItem = null
                infringementTextView.text = "Was the returned the item last time?"

            }
        }
    }

//     Function to search for the most recent issuance and update or create an infringement
//    call method after no is clicked for returned item
//    private fun processRecordsAndInfringements(empId: Int, itemDescription: String) {
//        Log.d("processRecordsAndInfringements Called", "$itemDescription, $empId")
//
//        searchIssuance(empId, itemDescription) { recordsData ->
//            Log.d("recordsData", recordsData.toString() )
//            if (recordsData != null) {
//
//                // Fetch the current infringement for the item description
//                val itemDescription = recordsData.itemDescription
//                fetchInfringement(empId, itemDescription) { infringement ->
//                    if (infringement != null) {
//                        // If an infringement object with the same item description exists, increase the count
//                        infringement.itemNotReturnedCount += 1
//                        updateInfringement(infringement)
//                    } else {
//                        // If no infringement object with the same item description exists, create a new one
//                        val newInfringement = InfringementsData(
//                            infringementId = "",
//                            empId = recordsData.empId,
//                            itemId = recordsData.itemId,
//                            itemDescription = recordsData.itemDescription,
//                            itemNotReturnedCount = 1
//                        )
//                        Log.d("infringement object", newInfringement.toString())
//                        createInfringement(newInfringement)
//
//                    }
//                }
//            }else{
//                //if the user is new and this is their first issuance for specific item
//            }
//
//        }
//    }
//
//    // Function to fetch the most recent issuance
//    private fun searchIssuance(empId_: Int, itemDescription_: String, callback: (RecordsData?) -> Unit) {
//        val recordsReference2: DatabaseReference = FirebaseDatabase.getInstance().getReference("issuance_records")
//        Log.d("query","$empId_ $itemDescription_")
//        // Query the database
//        val searchKey = "$empId_ $itemDescription_"
//        val str = searchKey.split(" ")
//        val searchKey1 = str[0].toInt()
//        val searchKey2 = str[1]
//        Log.d("searchKey1",searchKey1.toString())
//        //val query: Query = recordsReference.orderByChild("empId_itemId").equalTo("$empId_$itemDescription_").limitToLast(1)
//        val query: Query = recordsReference2.orderByChild("empId").equalTo(searchKey1.toDouble())
//        Log.d("query",query.toString())
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    val recordsList = mutableListOf<RecordsData>()
//
//                    Log.d("dataSnapshot",dataSnapshot.toString())
//                    for (recordSnapshot in dataSnapshot.children) {
//                        val record = recordSnapshot.getValue(RecordsData::class.java)
//                        if (record != null) {
//                            recordsList.add(record)
//                        }
//                    }
//
//                    Log.d("recordsList",recordsList.toString())
//                    // var filteredRecords = mutableListOf<RecordsData>()
//                    Log.d("recordsList",searchKey1.toString())
//                    val filteredRecords = recordsList.filter {  it.itemDescription.trim() == searchKey2.trim()  }
//                    Log.d("filteredRecords",filteredRecords.toString())
//
//                    // Find the most recent issuance
//                    val mostRecentIssuance = filteredRecords.maxByOrNull { it.issuanceDate }
//                    callback(mostRecentIssuance)
//                } else {
//                    // No matching records found
//                    callback(null)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle the error
//                callback(null)
//            }
//        })
//    }
//
//    // Function to fetch the current infringement for the item description
//   private fun fetchInfringement(empId_: Int, itemDescription_: String, callback: (InfringementsData?) -> Unit) {
//        // Query the database
//        val query: Query = infringementsReference.orderByChild("empId_itemId").equalTo("$empId_$itemDescription_").limitToLast(1)
//
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    val infringement = dataSnapshot.children.first().getValue(InfringementsData::class.java)
//                    callback(infringement)
//                } else {
//                    // No matching infringement found
//                    callback(null)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle the error
//                callback(null)
//            }
//        })
//    }
//
//    // Function to update an existing infringement
//    private fun updateInfringement(infringement: InfringementsData) {
//        infringementsReference.child(infringement.infringementId).setValue(infringement)
//    }
//
//    // Function to create a new infringement
//    private fun createInfringement(infringement: InfringementsData) {
//        val newInfringementRef = infringementsReference.push()
//        infringement.infringementId = newInfringementRef.key.toString() // Assign the generated key as the infringementId
//        Log.d("infringement object", infringement.toString())
//        newInfringementRef.setValue(infringement)
//    }
}
package com.example.parktown_manufacturers_ppe_tracking_application.Issuance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.EmployeeDetails.Infringements.InfringementsData
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsActivity
import com.example.parktown_manufacturers_ppe_tracking_application.Issuance.IssuanceRecords.RecordsData
import com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement.PpeItemData
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*

class RecordIssuanceActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var saveButton: Button

    private  var empId_: Int = 0
    private  var itemId: Int = 0
    private var itemDescription: String = ""
    private var previousItemReturned: Boolean = true
    private lateinit var employeeSpinner: Spinner
    private lateinit var itemNameSpinner: Spinner


    // the paths are not yet finalized e.g. (issuance_records and employee_infringements)
    val recordsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("issuance_records")
    val infringementsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("employee_infringements")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_issuance)

        backButton = findViewById(R.id.back_button)
        saveButton = findViewById(R.id.btnSave)
        employeeSpinner = findViewById(R.id.empName_Spinner)

        itemNameSpinner = findViewById(R.id.itemName_Spinner)


        backButton.setOnClickListener {
            val intent = Intent(this, RecordsActivity::class.java)
            startActivity(intent)
        }

        fetchItemNamesFromFirebase()

        saveButton.setOnClickListener {

            if(previousItemReturned){
                //yes it was returned

            }else{
                //No it was not returned
                processRecordsAndInfringements( 1001, "Skull Cap")
            }
            val intent = Intent(this, RecordsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchItemNamesFromFirebase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("ppeItems")

        // Listen for changes in the data
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val itemNames = mutableListOf<String>()

                for (snapshot in dataSnapshot.children) {
                    val ppeItemData = snapshot.getValue(PpeItemData::class.java)
                    ppeItemData?.itemName?.let {
                        itemNames.add(it)
                    }
                }

                // Populate the Spinner with the fetched item names
                populateItemNameSpinner(itemNames)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }

    private fun populateItemNameSpinner(itemNames: List<String>) {
        // Create an ArrayAdapter using the item names and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemNames.toTypedArray())

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        itemNameSpinner.adapter = adapter
    }

    // Function to search for the most recent issuance and update or create an infringement
    //call method after no is clicked for returned item
    private fun processRecordsAndInfringements(empId: Int, itemDescription: String) {
        searchIssuance(empId, itemDescription) { recordsData ->
            if (recordsData != null) {
                // Fetch the current infringement for the item description
                val itemDescription = recordsData.itemDescription
                fetchInfringement(empId, itemDescription) { infringement ->
                    if (infringement != null) {
                        // If an infringement object with the same item description exists, increase the count
                        infringement.itemNotReturnedCount += 1
                        updateInfringement(infringement)
                    } else {
                        // If no infringement object with the same item description exists, create a new one
                        val newInfringement = InfringementsData(
                            infringementId = "",
                            empId = recordsData.empId,
                            itemId = recordsData.itemId,
                            itemDescription = recordsData.itemDescription,
                            itemNotReturnedCount = 1
                        )
                        createInfringement(newInfringement)
                    }
                }
            }else{
                //if the user is new and this is their first issuance for specific item
            }

        }
    }

    // Function to fetch the most recent issuance
    private fun searchIssuance(empId_: Int, itemDescription_: String, callback: (RecordsData?) -> Unit) {
        // Query the database
        val query: Query = recordsReference.orderByChild("empId_itemId").equalTo("$empId_$itemDescription_").limitToLast(1)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val recordsList = mutableListOf<RecordsData>()

                    for (recordSnapshot in dataSnapshot.children) {
                        val record = recordSnapshot.getValue(RecordsData::class.java)
                        if (record != null) {
                            recordsList.add(record)
                        }
                    }

                    // Find the most recent issuance
                    val mostRecentIssuance = recordsList.maxByOrNull { it.issuanceDate }
                    callback(mostRecentIssuance)
                } else {
                    // No matching records found
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                callback(null)
            }
        })
    }

    // Function to fetch the current infringement for the item description
   private fun fetchInfringement(empId_: Int, itemDescription_: String, callback: (InfringementsData?) -> Unit) {
        // Query the database
        val query: Query = infringementsReference.orderByChild("empId_itemId").equalTo("$empId_$itemDescription_").limitToLast(1)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val infringement = dataSnapshot.children.first().getValue(InfringementsData::class.java)
                    callback(infringement)
                } else {
                    // No matching infringement found
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                callback(null)
            }
        })
    }

    // Function to update an existing infringement
    private fun updateInfringement(infringement: InfringementsData) {
        infringementsReference.child(infringement.infringementId).setValue(infringement)
    }

    // Function to create a new infringement
    private fun createInfringement(infringement: InfringementsData) {
        val newInfringementRef = infringementsReference.push()
        infringement.infringementId = newInfringementRef.key.toString() // Assign the generated key as the infringementId
        newInfringementRef.setValue(infringement)
    }
}
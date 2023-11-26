package com.example.parktown_manufacturers_ppe_tracking_application

//package com.example.parktown_manufacturers_ppe_tracking_application.Employee

import android.content.Intent
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import com.example.parktown_manufacturers_ppe_tracking_application.Employee.AddEmployeeActivity
import com.example.parktown_manufacturers_ppe_tracking_application.R
import com.google.firebase.database.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class AddEmployeeActivityTest {

    @Mock
    private lateinit var databaseReference: DatabaseReference

    @Mock
    private lateinit var empIdEditText: EditText

    @Mock
    private lateinit var empNameEditText: EditText

    @Mock
    private lateinit var empSurnameEditText: EditText

    @Mock
    private lateinit var btnSave: Button

    @Mock
    private lateinit var backButton: Button

    @Mock
    private lateinit var empDepartmentNameSpinner: Spinner

    @Mock
    private lateinit var toolbar: Toolbar

    @InjectMocks
    private lateinit var activity: AddEmployeeActivity

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test saveBtn method`() {
        `when`(empNameEditText.text).thenReturn(mock(Editable::class.java).apply {
            `when`(toString()).thenReturn("John")
        })

        `when`(empSurnameEditText.text).thenReturn(mock(Editable::class.java).apply {
            `when`(toString()).thenReturn("Doe")
        })

        `when`(empDepartmentNameSpinner.selectedItem.toString()).thenReturn("Welding")

        val dataSnapshot = mock(DataSnapshot::class.java)
        `when`(databaseReference.orderByChild("employeeFullName")).thenReturn(databaseReference)
        `when`(databaseReference.equalTo("John Doe")).thenReturn(databaseReference)

        `when`(databaseReference.addListenerForSingleValueEvent(any(ValueEventListener::class.java))).thenAnswer {
            val valueEventListener = it.arguments[0] as ValueEventListener
            valueEventListener.onDataChange(dataSnapshot)
            null
        }

        activity.saveBtn()

        verify(databaseReference).push()
    }

    @Test
    fun `test empID method`() {
        val dataSnapshot = mock(DataSnapshot::class.java)
        val employeeSnapshot = mock(DataSnapshot::class.java)
        `when`(dataSnapshot.exists()).thenReturn(true)
        `when`(dataSnapshot.children).thenReturn(listOf(employeeSnapshot))
        `when`(employeeSnapshot.child("employeeId").getValue(Int::class.java)).thenReturn(1000)

        `when`(databaseReference.orderByChild("employeeId")).thenReturn(databaseReference)
        `when`(databaseReference.limitToLast(1)).thenReturn(databaseReference)

        `when`(databaseReference.addListenerForSingleValueEvent(any(ValueEventListener::class.java))).thenAnswer {
            val valueEventListener = it.arguments[0] as ValueEventListener
            valueEventListener.onDataChange(dataSnapshot)
            null
        }

        activity.empID(databaseReference)

        verify(empIdEditText).setText("1001")
    }

    @Test
    fun `test empID method when no employee exists`() {
        val dataSnapshot = mock(DataSnapshot::class.java)

        `when`(dataSnapshot.exists()).thenReturn(false)

        `when`(databaseReference.orderByChild("employeeId")).thenReturn(databaseReference)
        `when`(databaseReference.limitToLast(1)).thenReturn(databaseReference)

        `when`(databaseReference.addListenerForSingleValueEvent(any(ValueEventListener::class.java))).thenAnswer {
            val valueEventListener = it.arguments[0] as ValueEventListener
            valueEventListener.onDataChange(dataSnapshot)
            null
        }

        activity.empID(databaseReference)

        verify(empIdEditText).setText("1001")
    }

    @Test
    fun `test onCreate method`() {
        val intent = mock(Intent::class.java)
        `when`(intent.getIntExtra("empId", 0)).thenReturn(1)
        `when`(activity.intent).thenReturn(intent)

        activity.onCreate(null)

        verify(activity).setContentView(R.layout.activity_add_employee)
        verify(activity).findViewById<Toolbar>(R.id.toolbar)
        verify(activity).findViewById<Button>(R.id.btnSave)
        verify(activity).findViewById<Button>(R.id.add_employee_backbutton)
        verify(activity).findViewById<EditText>(R.id.empId_EditText)
        verify(activity).findViewById<EditText>(R.id.empName_EditText)
        verify(activity).findViewById<EditText>(R.id.empSurname_EditText)
        verify(activity).findViewById<Spinner>(R.id.departmentName_Spinner)

        verify(activity).empID(databaseReference)
        verify(activity.btnSave).setOnClickListener(any())
        verify(activity.backButton).setOnClickListener(any())
    }
}

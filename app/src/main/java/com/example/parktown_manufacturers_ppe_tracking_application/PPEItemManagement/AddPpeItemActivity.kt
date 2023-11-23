package com.example.parktown_manufacturers_ppe_tracking_application.PPEItemManagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.parktown_manufacturers_ppe_tracking_application.R

class AddPpeItemActivity : AppCompatActivity() {
    private lateinit var backButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ppe_item)

        backButton = findViewById(R.id.add_ppe_backbutton)

        backButton.setOnClickListener {
            val intent = Intent(this, PpeItemsActivity::class.java)
            startActivity(intent)
        }
    }
}
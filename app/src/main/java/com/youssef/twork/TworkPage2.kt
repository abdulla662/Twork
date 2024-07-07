package com.youssef.twork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TworkPage2 : AppCompatActivity() {
    // Declare FirebaseAuth and FirebaseFirestore variables
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // Declare other UI elements
    private lateinit var username1: EditText
    private lateinit var pass: EditText
    private lateinit var cpass: EditText
    private lateinit var email: EditText
    private lateinit var spinner: Spinner
    private lateinit var signup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twork_page2)

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize FirebaseFirestore instance
        firestore = FirebaseFirestore.getInstance()

        // Initialize other UI elements
        username1 = findViewById(R.id.username1)
        pass = findViewById(R.id.pass)
        cpass = findViewById(R.id.cpass)
        email = findViewById(R.id.email)
        spinner = findViewById(R.id.spinner)
        signup = findViewById(R.id.signup)

        // Set up Spinner
        val items = arrayOf("employee", "company")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Handle item selection in Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // You can perform actions based on the selected item here if needed
                val selectedItem = items[position]
                Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Set onClickListener for signup button
        // Set onClickListener for signup button
        signup.setOnClickListener {
            val emailStr = email.text.toString()
            val nameStr = username1.text.toString()
            val passStr = pass.text.toString()
            val passRetypeStr = cpass.text.toString()

            if (emailStr.isNotEmpty() && passStr.isNotEmpty() && nameStr.isNotEmpty() && passRetypeStr.isNotEmpty()) {
                if (passStr == passRetypeStr) {
                    firebaseAuth.createUserWithEmailAndPassword(emailStr, passStr)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                val userData = hashMapOf(
                                    "name" to nameStr,
                                    "user" to true
                                )
                                user?.uid?.let { userId ->
                                    // Determine the collection based on the selected item
                                    val collection = if (spinner.selectedItem == "employee") {
                                        "employees"
                                    } else {
                                        "companies"
                                    }
                                    // Add user data to Firestore
                                    firestore.collection(collection).document(userId)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            // Set user type
                                            val userType = if (spinner.selectedItem == "employee") {
                                                "employee"
                                            } else {
                                                "company"
                                            }
                                            // Update user type in Firestore
                                            val userTypeData = hashMapOf(
                                                "userType" to userType
                                            )
                                            firestore.collection(collection).document(userId)
                                                .update(userTypeData as Map<String, Any>)
                                                .addOnSuccessListener {
                                                    // Navigate to the appropriate activity
                                                    val intent =
                                                        if (spinner.selectedItem == "employee") {
                                                            Intent(this, MainActivity::class.java)
                                                        } else {
                                                            Intent(this, MainActivity::class.java)
                                                        }
                                                    startActivity(intent)
                                                    // Finish the current activity
                                                    finish()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        this,
                                                        "Error setting user type in Firestore: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this,
                                                "Error adding user data to Firestore: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}

package com.youssef.twork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

lateinit var firbaseauth:FirebaseAuth
lateinit var firestore: FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        db = Firebase.firestore

    }

    fun signup(view: View) {
        val a = Intent(this, TworkPage2::class.java)
        startActivity(a)
    }

    fun signin(view: View) {
        val emailStr = username.text.toString()
        val passStr = password.text.toString()

        if (emailStr.isNotEmpty() && passStr.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        user?.uid?.let { userId ->
                            // Determine the collection based on the user type
                            firestore.collection("employees").document(userId).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val intent = Intent(this, Twork3::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // If not found in employees collection, check in companies collection
                                        firestore.collection("companies").document(userId).get()
                                            .addOnSuccessListener { companyDocument ->
                                                if (companyDocument.exists()) {
                                                    val intent =
                                                        Intent(this, mainpagecompany::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                } else {
                                                    Toast.makeText(
                                                        this,
                                                        "User data not found",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(
                                                    this,
                                                    "Error retrieving user data from Firestore: ${e.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Error retrieving user data from Firestore: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Please enter both username and password",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}

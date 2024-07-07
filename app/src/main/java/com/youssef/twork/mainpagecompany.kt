package com.youssef.twork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class mainpagecompany : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var nameuser: TextView
    private lateinit var posts: TextView
    private lateinit var jobadd: TextView
    private lateinit var forms: TextView
    private var userid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpagecompany) // Ensure this is the correct layout file

        // Initialize FirebaseAuth and FirebaseFirestore
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get current user ID
        userid = firebaseAuth.currentUser?.uid

        // Initialize views
        nameuser = findViewById(R.id.nameuser)
        posts = findViewById(R.id.posts)
        jobadd = findViewById(R.id.jobadd)
        forms = findViewById(R.id.forms)

        // Get user name from Firestore
        getUserName()
    }

    private fun getUserName() {
        // Ensure userid is not null before making the Firestore call
        userid?.let {
            db.collection("companies").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        nameuser.text = "Welcome $name"
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting document: ", exception)
                }
        } ?: Log.w("Firestore", "User ID is null")
    }

    fun jobposts(view: View) {
        val a = Intent(this,tworkcompany::class.java)
        startActivity(a)
    }

    fun addjob(view: View) {
        val a = Intent(this,comanyactivity2::class.java)
        startActivity(a)
    }

    fun forms(view: View) {
        val a = Intent(this,FormsActivity::class.java)
        startActivity(a)
    }
}

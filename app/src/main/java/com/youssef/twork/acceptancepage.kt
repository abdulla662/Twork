package com.youssef.twork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class acceptancepage : AppCompatActivity() {
    lateinit var nametext: TextView
    lateinit var agetext: TextView
    lateinit var listtext: TextView
    lateinit var abouttext: TextView
    lateinit var coursestext: TextView
    lateinit var accept: Button
    lateinit var reject: Button
    private var currentUser = FirebaseAuth.getInstance().currentUser
    var x: String = ""
    var number:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceptancepage)
        nametext = findViewById(R.id.nametext)
        agetext = findViewById(R.id.agetext)
        listtext = findViewById(R.id.listtext)
        abouttext = findViewById(R.id.abouttext)
        coursestext = findViewById(R.id.coursestext)
        accept = findViewById(R.id.accept)
        reject = findViewById(R.id.reject)
        getDataFromRealtimeDatabase()
         number = intent.getStringExtra("number").toString()
        accept.setOnClickListener {
            val a=Intent(this@acceptancepage,Twork5::class.java)
            a.putExtra("number",number)
            startActivity(a)
        }
        reject.setOnClickListener {
            rejectAndDeleteChild()
        }
    }

    private fun getDataFromRealtimeDatabase() {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val companyId = snapshot.child("companyid").getValue(String::class.java)
                    Log.d("FormsActivity", "Current User ID: ${currentUser?.uid}, Company ID: $companyId")

                    if (currentUser?.uid == companyId) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: "N/A"
                        val age = snapshot.child("age").getValue(String::class.java) ?: "N/A"
                        val about = snapshot.child("about").getValue(String::class.java) ?: "N/A"
                        val courses = snapshot.child("courses").getValue(String::class.java) ?: "N/A"
                        val list = snapshot.child("list").getValue(String::class.java) ?: "N/A"

                        // Update EditText fields
                        nametext.setText(name)
                        agetext.setText(age)
                        abouttext.setText(about)
                        coursestext.setText(courses)
                        listtext.setText(list)
                    } else {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
    }

    private fun rejectAndDeleteChild() {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val moreInfoValue = number

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val companyId = snapshot.child("companyid").getValue(String::class.java)
                    val infoValue = snapshot.child("moreinfo").getValue(String::class.java)

                    if (currentUser?.uid == companyId && moreInfoValue == infoValue) {
                        // Delete the child node
                        snapshot.ref.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@acceptancepage, "Record deleted successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@acceptancepage, "Failed to delete record", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}

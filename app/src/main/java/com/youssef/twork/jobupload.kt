package com.youssef.twork

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database


class jobupload : AppCompatActivity() {
    lateinit var nametext: EditText
    lateinit var agetext: EditText
    lateinit var listtext: EditText
    lateinit var abouttext: EditText
    lateinit var coursestext: EditText
    lateinit var moreinfo: EditText
    lateinit var button: Button
    var userID1: String = ""
    var companyid: String = ""
    var wantedjob1: String = ""


    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobupload)
        nametext = findViewById(R.id.nametext)
        agetext = findViewById(R.id.agetext)
        listtext = findViewById(R.id.listtext)
        abouttext = findViewById(R.id.abouttext)
        coursestext = findViewById(R.id.coursestext)
        button = findViewById(R.id.button)
        moreinfo = findViewById(R.id.moreinfo)
        database = Firebase.database.reference
        userID1 = intent.getStringExtra("userid").toString()
        companyid = intent.getStringExtra("companyid").toString()
        wantedjob1 = intent.getStringExtra("wantedjob").toString()

        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        button.setOnClickListener {
            writeNewUser(userID1)
        }
    }

    fun writeNewUser(userID: String) {
        database.child("users").child(userID)
        val name = nametext.text.toString()
        val age = agetext.text.toString()
        val list = listtext.text.toString()
        val about = abouttext.text.toString()
        val courses = coursestext.text.toString()
        val moreInfo = moreinfo.text.toString()
        val userId1 = userID1
        val companyId = companyid
        if (name.isEmpty() || age.isEmpty() || list.isEmpty() || about.isEmpty() || courses.isEmpty() || moreInfo.isEmpty() || userId1.isEmpty() || companyId.isEmpty()) {
            // Show error message
            Toast.makeText(
                this@jobupload,
                "Please fill in all fields before submitting",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Create a hashmap with the user data
            val data = hashMapOf(
                "name" to name,
                "age" to age,
                "list" to list,
                "about" to about,
                "courses" to courses,
                "moreinfo" to moreInfo,
                "userid" to userId1,
                "companyid" to companyId,
                "jobtitle" to wantedjob1
            )

            // Set the value for the new user node
            database.child("users").child(userID).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Data successfully written
                        Toast.makeText(
                            this@jobupload,
                            "Your job was posted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Handle the error
                        Toast.makeText(this@jobupload, "Please try again later", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}



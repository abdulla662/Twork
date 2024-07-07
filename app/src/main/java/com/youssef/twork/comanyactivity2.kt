package com.youssef.twork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class comanyactivity2 : AppCompatActivity() {
   private lateinit var companyname:EditText
   private lateinit var location:EditText
    private lateinit var jobdescription:EditText
    private lateinit var jobpostion:EditText
    private lateinit var email:EditText
    private lateinit var employmentType:EditText
    private lateinit var number:EditText
    private lateinit var typeofworkplace:EditText

    lateinit var upload:Button
    private val db = FirebaseFirestore.getInstance()
    var userID = Firebase.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comanyactivity2)
        location=findViewById(R.id.location)
        companyname=findViewById(R.id.companyname)
        jobdescription=findViewById(R.id.jobdescription)
        jobpostion=findViewById(R.id.jobpostion)
        number=findViewById(R.id.number)
        employmentType=findViewById(R.id.employmentType)
        upload=findViewById(R.id.upload)
        typeofworkplace=findViewById(R.id.typeofworkplace)
        email=findViewById(R.id.email)

        readDataFromFirestore()

        upload.setOnClickListener {
            uploadDataToFirestore()
        }
    }

    private fun uploadDataToFirestore() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
            return
        }

        val data = hashMapOf(
            "jobposition" to jobpostion.text.toString(),
            "company" to companyname.text.toString(),
            "employmentType" to employmentType.text.toString(),
            "description" to jobdescription.text.toString(),
            "number" to number.text.toString(),
            "email" to email.text.toString(),
            "companyid" to userID.toString(),
            "joblocation" to location.text.toString(),
            "typeofworkplace" to typeofworkplace.text.toString()
        )

        db.collection("companydata").document(user.uid).set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Data uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun readDataFromFirestore() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
            return
        }


    }
}

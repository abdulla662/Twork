package com.youssef.twork

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Twork3 : AppCompatActivity() {
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    lateinit var itemLayoutView1: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twork3)
        getDataFromFirestore()
    }

    private fun getDataFromFirestore() {
        db.collection("companydata")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val wantedJob = document.getString("jobposition") ?: "N/A"
                    val jobLocation = document.getString("joblocation") ?: "N/A"
                    val company = document.getString("company") ?: "N/A"
                    val documentId = document.id


                    val inflater = layoutInflater
                    val itemLayoutView = inflater.inflate(R.layout.item_layout, null)

                    val textViewCompany = itemLayoutView.findViewById<TextView>(R.id.textView)
                    val textViewEmployeeWork = itemLayoutView.findViewById<TextView>(R.id.textView47)
                    val textViewJobLocation = itemLayoutView.findViewById<TextView>(R.id.textView50)

                    textViewCompany.text = company
                    textViewEmployeeWork.text = wantedJob
                    textViewJobLocation.text = jobLocation

                    val clickListener = View.OnClickListener {
                        Toast.makeText(this@Twork3, "Document ID: $documentId", Toast.LENGTH_SHORT).show()
                        val a = Intent(this@Twork3, twork4::class.java)
                        a.putExtra("userid", documentId)
                        startActivity(a)
                    }

                    textViewCompany.setOnClickListener(clickListener)
                    textViewEmployeeWork.setOnClickListener(clickListener)
                    textViewJobLocation.setOnClickListener(clickListener)

                    val itemLayoutContainer = findViewById<LinearLayout>(R.id.itemLayoutView1)
                    itemLayoutContainer.addView(itemLayoutView)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
            }
    }


}
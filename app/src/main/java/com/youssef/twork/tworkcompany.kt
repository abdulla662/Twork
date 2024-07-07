package com.youssef.twork

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class tworkcompany : AppCompatActivity() {
lateinit var itemLayoutView:LinearLayout
    private val db = FirebaseFirestore.getInstance()
    lateinit var uploadbtt: Button
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tworkcompany)
        getDataFromFirestore()
        itemLayoutView=findViewById(R.id.itemLayoutView1)

    
    }

    fun addition(view: View) {
        val a = Intent(this, comanyactivity2::class.java)
        startActivity(a)
    }

    private fun getDataFromFirestore() {
        Log.d(TAG, "Fetching data from Firestore")
        db.collection("companydata")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Documents fetched successfully, count: ${result.size()}")
                val itemLayoutContainer = findViewById<LinearLayout>(R.id.itemLayoutView1)
                itemLayoutContainer.removeAllViews()

                for (document in result) {
                    Log.d(TAG, "Processing document: ${document.id}")
                    val wantedJob = document.getString("jobposition") ?: "N/A"
                    val jobLocation = document.getString("company") ?: "N/A"
                    val company = document.getString("joblocation") ?: "N/A"

                    val inflater = layoutInflater
                    val itemLayoutView = inflater.inflate(R.layout.item_layout, null, false) // false to not attach to root
                    val textViewCompany = itemLayoutView.findViewById<TextView>(R.id.textView)
                    val textViewEmployeeWork = itemLayoutView.findViewById<TextView>(R.id.textView47)
                    val textViewJobLocation = itemLayoutView.findViewById<TextView>(R.id.textView50)

                    textViewCompany.text = wantedJob
                    textViewEmployeeWork.text = jobLocation
                    textViewJobLocation.text = company

                    itemLayoutContainer.addView(itemLayoutView)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
            }
    }

    fun gooo(view: View) {
        val a = Intent(this@tworkcompany, FormsActivity::class.java)
        startActivity(a)
    }

}




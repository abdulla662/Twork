package com.youssef.twork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FormsActivity : AppCompatActivity() {

    private lateinit var itemLayoutContainer: LinearLayout
    private var currentUser = FirebaseAuth.getInstance().currentUser
    var x:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms)
        itemLayoutContainer = findViewById(R.id.itemLayoutView1)
        getDataFromFirestore()
    }

    private fun getDataFromFirestore() {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemLayoutContainer.removeAllViews() // Clear the container before adding new views
                for (snapshot in dataSnapshot.children) {
                    val companyId = snapshot.child("companyid").getValue(String::class.java)
                    Log.d("FormsActivity", "Current User ID: ${currentUser?.uid}, Company ID: $companyId")
                    if (currentUser?.uid == companyId) {
                       x=snapshot.child("moreinfo").getValue(String::class.java) ?: "N/A"

                        addFormToLayout(
                            snapshot.child("jobtitle").getValue(String::class.java) ?: "N/A" ,
                            snapshot.child("name").getValue(String::class.java) ?: "N/A",
                            snapshot.child("age").getValue(String::class.java) ?: "N/A"
                        )
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

    private fun addFormToLayout(position: String, name: String, age: String) {
        val inflater = LayoutInflater.from(this)
        val itemLayoutView = inflater.inflate(R.layout.formsorders, null)

        val textViewPosition = itemLayoutView.findViewById<TextView>(R.id.jobmain)
        val textViewName = itemLayoutView.findViewById<TextView>(R.id.name)
        val textViewAge = itemLayoutView.findViewById<TextView>(R.id.age)

        textViewPosition.text = position
        textViewName.text = name
        textViewAge.text = age

        val clickListener = View.OnClickListener {
            val a=Intent(this,acceptancepage::class.java)
            a.putExtra("number",x)
            startActivity(a)
        }

        textViewPosition.setOnClickListener(clickListener)
        textViewName.setOnClickListener(clickListener)
        textViewAge.setOnClickListener(clickListener)



        itemLayoutContainer.addView(itemLayoutView)
    }
}

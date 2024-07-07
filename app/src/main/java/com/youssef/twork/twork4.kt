package com.youssef.twork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Path
import android.graphics.PathMeasure
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class twork4 : AppCompatActivity() {
    lateinit var textView2: TextView
    private lateinit var db: FirebaseFirestore
    lateinit var jobtitle: TextView
    lateinit var company: TextView
    lateinit var description: TextView
    lateinit var employeeWork: TextView
    lateinit var employmentType: TextView
    lateinit var joblocation: TextView
    lateinit var button5:Button
    lateinit var imageView12:ImageView
    var wantedjob:String=""
    var userID = Firebase.auth.currentUser?.uid
    var companyid:String=""
   lateinit var back:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twork4)
        db = FirebaseFirestore.getInstance()
        jobtitle = findViewById(R.id.jobtitle)
        company = findViewById(R.id.company)
        description = findViewById(R.id.description)
        employeeWork = findViewById(R.id.employeeWork)
        employmentType = findViewById(R.id.employmentType)
        joblocation = findViewById(R.id.joblocation)
        button5=findViewById(R.id.button5)


        var userID1 = Firebase.auth.currentUser?.uid
       animateToLogo()
        button5.setOnClickListener {
            val intent = Intent(this, jobupload::class.java).apply {
                putExtra("userid", userID1)
                putExtra("companyid",companyid)
                putExtra("wantedjob",wantedjob)

                startActivity(intent)
            }
            startActivity(intent)
        }

        val userID = intent.getStringExtra("userid")
        if (userID != null) {
            db.collection("companydata").document(userID)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val companyText = document.getString("company")
                        val descriptionText = document.getString("description")
                        val employeeWorkText = document.getString("employeeWork")
                        val employmentTypeText = document.getString("employmentType")
                        val jobLocation = document.getString("joblocation")
                        val position=document.getString("jobposition")
                        val type=document.getString("typeofworkplace")
                        wantedjob = document.getString("wantedJob").toString()
                         companyid= document.getString("companyid").toString()

                        // Update TextViews with data from Firestore
                        jobtitle.text = position
                        company.text = companyText
                        description.text = descriptionText
                        employeeWork.text = employeeWorkText
                        employmentType.text = employmentTypeText
                        joblocation.text = jobLocation
                        employeeWork.text=type

                    } else {
                        Log.d("twork4", "No such document")
                        textView2.text = "No data found"
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("twork4", "Error getting documents.", e)
                    textView2.text = "Error loading data"
                }
        } else {
            Log.e("twork4", "No user ID received in intent")
            textView2.text = "User ID is missing."
        }
    }






    private fun animateToLogo() {
        val imageView12 = findViewById<ImageView>(R.id.imageView12)
        val imageView13 = findViewById<ImageView>(R.id.imageView13)

        imageView12.post {
            // Get locations on screen for both ImageViews
            val location12 = IntArray(2)
            imageView12.getLocationOnScreen(location12)
            val location13 = IntArray(2)
            imageView13.getLocationOnScreen(location13)

            // Adjust the endX to exactly match the horizontal center of imageView13
            val endX = location13[0] + imageView13.width / 2f - imageView12.width / 2f
            // Adjust endY so that imageView12 is just above imageView13, adding an offset to stop higher
            val endY = location13[1] - imageView12.height - 50  // Adjust the offset as needed

            // Calculate start and end points
            val startX = location12[0].toFloat() + imageView12.width / 2f
            val startY = location12[1].toFloat()

            // Create a path for the animation
            val path = Path().apply {
                moveTo(startX, startY)  // Start from the current position of imageView12
                val midX = (startX + endX) / 2
                val midY = startY - 300  // This is an arbitrary height to create an arc, adjust as necessary

                // Use a quadratic Bezier curve to create a smooth arc transition
                quadTo(midX, midY, endX, endY.toFloat())
            }

            // Animate imageView12 along the path
            val animator = ObjectAnimator.ofFloat(imageView12, "x", "y", path).apply {
                duration = 3000  // Duration of the animation
                interpolator = LinearInterpolator()  // For smooth animation
            }
            animator.start()
        }
    }

    }














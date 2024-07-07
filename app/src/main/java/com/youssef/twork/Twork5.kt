    package com.youssef.twork

    import android.content.Intent
    import android.net.Uri
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import android.widget.TextView
    import com.google.firebase.firestore.FirebaseFirestore

    class Twork5 : AppCompatActivity() {
        private lateinit var db: FirebaseFirestore
        lateinit var email:TextView
        lateinit var phonenumber:TextView
        var number1:String=""
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_twork5)
            email=findViewById(R.id.email)
            phonenumber=findViewById(R.id.phonenumber)
            db = FirebaseFirestore.getInstance()
            val userID = intent.getStringExtra("userid")
             number1 = intent.getStringExtra("number").toString()


            if (userID != null) {
                // Fetch data from Firestore
                db.collection("companydata").document(userID)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val companyText = document.getString("email")
                            val descriptionText = document.getString("number")
                            email.text = companyText
                            phonenumber.text = descriptionText


                        } else {
                            Log.d("twork4", "No such document")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("twork4", "Error getting documents.", e)
                    }
            } else {
                Log.e("twork4", "No user ID received in intent")
            }
        }

        fun call(view: View) {
            val phoneNumberWithCountryCode = "+20" + number1

            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumberWithCountryCode")
                    setPackage("com.whatsapp")
                }
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("WhatsAppError", "Failed to open WhatsApp: ", e)
            }
        }



    }





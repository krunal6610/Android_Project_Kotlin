package com.example.notes_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var loginActivity: TextView
    lateinit var edtEmail: TextInputEditText
    lateinit var edtPassword: TextInputEditText
    lateinit var edtName: TextInputEditText
    lateinit var signupBtn: Button
    lateinit var gLoginBtn: Button

    lateinit var auth:FirebaseAuth
    lateinit var mRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        loginActivity = findViewById(R.id.loginActivity)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        signupBtn = findViewById(R.id.signupBtn)
        gLoginBtn = findViewById(R.id.gLoginBtn)

       // mRef = FirebaseDatabase.getInstance().getReference("UserDetails")

        mRef = FirebaseDatabase.getInstance().getReference("Email")


        auth = FirebaseAuth.getInstance()

       // firestore = FirebaseFirestore.getInstance()


        loginActivity.setOnClickListener {

            startActivity(Intent(this,LoginActivity::class.java))

        }

        signupBtn.setOnClickListener {

            var name = edtName.text.toString()
            var email = edtEmail.text.toString()
            var password = edtPassword.text.toString()




            if (name.isNotEmpty()&& email.isNotEmpty() && password.isNotEmpty()){

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        task->
                    if (task.isSuccessful){

                      //  firestore.collection("Users").document(email).set(password)



                        var uData = uDetails(name,email,password)
                        mRef.child(auth.currentUser!!.uid).child("UserDetails").setValue(uData)


                        Toast.makeText(this,"Successfull",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()

                    }else{

                        Toast.makeText(this,"Error!",Toast.LENGTH_SHORT).show()
                    }


                }

            }else{

                Toast.makeText(this,"Please enter the all details!",Toast.LENGTH_SHORT).show()
            }


        }




    }

         data class uDetails(

             var name:String?=null,
             var email:String?=null,
             var password:String?=null

         )

}
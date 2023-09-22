package com.example.notes_firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {



    lateinit var registerActivity:TextView
    lateinit var edtEmail:TextInputEditText
    lateinit var edtPassword:TextInputEditText
    lateinit var loginBtn:Button
    lateinit var gLoginBtn:Button


    lateinit var auth:FirebaseAuth
    lateinit var mRef: DatabaseReference

    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

       // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        registerActivity = findViewById(R.id.registerActivity)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        loginBtn = findViewById(R.id.loginBtn)
        gLoginBtn = findViewById(R.id.gLoginBtn)

        auth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().getReference("Email")

        registerActivity.setOnClickListener {

            startActivity(Intent(this,RegisterActivity::class.java))

        }

        loginBtn.setOnClickListener {


            var email = edtEmail.text.toString()
            var password = edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                     task->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Successfull",Toast.LENGTH_SHORT).show()
                        var i = Intent(this,HomeActivity::class.java)
                        intent.putExtra("email" , email)
                        startActivity(i)
                        finish()

                    }else{

                        Toast.makeText(this,"Error!",Toast.LENGTH_SHORT).show()
                    }



                }

                }else{

                Toast.makeText(this,"Error!",Toast.LENGTH_SHORT).show()
            }


        }



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        gLoginBtn.setOnClickListener {


          //  startActivity(Intent(this,HomeActivity::class.java))

            signInGoogle()
        }

    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){

                var uData = RegisterActivity.uDetails(account.displayName,account.email,"pass")
                mRef.child(auth.currentUser!!.uid).child("UserDetails").setValue(uData)

                val intent : Intent = Intent(this , HomeActivity::class.java)
                intent.putExtra("email" , account.email)
                intent.putExtra("name" , account.displayName)

                startActivity(intent)

            }else{
                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){

            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
            finish()


        }
    }

   // override fun onStart() {
     //   super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
   //     var currentUser = auth.getCurrentUser()
   //    if (currentUser?.uid!!.isNotEmpty()){
///
    //        var intent : Intent = Intent(this , HomeActivity::class.java)
//
   //         startActivity(intent)
   //    }
      //  updateUI(currentUser);
  //  }

    data class uDetails(

        var name:String?=null,
        var email:String?=null,
        var password:String?=null

    )

}
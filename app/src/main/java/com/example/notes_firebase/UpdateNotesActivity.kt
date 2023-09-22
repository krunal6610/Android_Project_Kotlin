package com.example.notes_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateNotesActivity : AppCompatActivity() {


    lateinit var backBtn: ImageView
    lateinit var saveBtn: ImageView
    lateinit var deleteBtn: ImageView

    lateinit var edtTitle: EditText
    lateinit var edtNotes: EditText

    lateinit var mReference: DatabaseReference
    lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_notes)

        backBtn = findViewById(R.id.backBtn)
        saveBtn = findViewById(R.id.saveBtn)
        deleteBtn = findViewById(R.id.deletBtn)
        edtTitle = findViewById(R.id.edtTitle)
        edtNotes = findViewById(R.id.edtNotes)

        mReference = FirebaseDatabase.getInstance().getReference("Email")
        mAuth = FirebaseAuth.getInstance()

      //  val email = intent.getStringExtra("email")
//        if (email != null) {
//            mReference = FirebaseDatabase.getInstance().getReference(email.replace(".","").toString())
//        }

        var title = intent.getStringExtra("name")
        var notes = intent.getStringExtra("notes")
        var id = intent.getStringExtra("id")

        edtTitle.setText(title)
        edtNotes.setText(notes)

        backBtn.setOnClickListener {

            super.onBackPressed()

        }

        saveBtn.setOnClickListener {

            var title = edtTitle.text.toString()
            var notes = edtNotes.text.toString()

            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault())

            var date = Date()

            var getCurrentDateTime:String =   dateFormat.format(date)

            var fiDate = getCurrentDateTime.toString()

         //   var id = mReference.push().key!!
            var data = Model(id,title,notes,fiDate)

            mReference.child(mAuth.currentUser!!.uid).child("Notes").child(id!!).setValue(data).addOnCompleteListener {

                edtNotes.text.clear()
                edtTitle.text.clear()
                Toast.makeText(this,"Notes Updated Successfully", Toast.LENGTH_SHORT).show()
                var i = Intent(this,HomeActivity::class.java)
                startActivity(i)
              //  finish()

            }

                .addOnFailureListener {

                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()

                }


        }

        deleteBtn.setOnClickListener {

            mReference.child(mAuth.currentUser!!.uid).child("Notes").child(id!!).removeValue()
            edtNotes.text.clear()
            edtTitle.text.clear()
            Toast.makeText(this,"Notes Deleted Successfully",Toast.LENGTH_SHORT).show()
            var i = Intent(this,HomeActivity::class.java)
            startActivity(i)

        }

    }
}
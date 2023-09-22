package com.example.notes_firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    lateinit var profile_image:CircleImageView
    lateinit var nameTv:TextView
    lateinit var emailTv:TextView
    lateinit var edtPhotoBtn:Button
    lateinit var backImgBtn:ImageButton

    lateinit var auth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var imgUi:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profile_image = findViewById(R.id.profile_image)
        nameTv = findViewById(R.id.nameTv)
        emailTv = findViewById(R.id.emailTv)
        edtPhotoBtn = findViewById(R.id.edtPhotoBtn)
        backImgBtn = findViewById(R.id.backImgBtn)

      //  mRef = FirebaseDatabase.getInstance().getReference("UserDetails")

        mRef = FirebaseDatabase.getInstance().getReference("Email")


        auth = FirebaseAuth.getInstance()

        mRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {



                var data = snapshot.child(auth.currentUser!!.uid).child("UserDetails")

                nameTv.text = data.child("name").getValue(String::class.java)
                emailTv.text = data.child("email").getValue(String::class.java)
                imgUi = data.child("img").getValue(String::class.java).toString()

                Picasso.get().load(imgUi).placeholder(R.drawable.loading).into(profile_image)

                Toast.makeText(this@ProfileActivity,"sucess",Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ProfileActivity,"Error",Toast.LENGTH_SHORT).show()
            }


        })



        var imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it ->

            if (it.resultCode==Activity.RESULT_OK){

                if (it.data!=null){

                   var ref = Firebase.storage.reference.child("profilePhoto")

                    ref.putFile(it.data!!.data!!).addOnSuccessListener {

                        Toast.makeText(this,"img not upload",Toast.LENGTH_SHORT).show()

                        ref.downloadUrl.addOnSuccessListener {

                            mRef.child(auth.currentUser!!.uid).child("UserDetails").child("img").setValue(it.toString())

                        //  Picasso.get().load(imgUi).into(profile_image)

                            Toast.makeText(this,"img fetch",Toast.LENGTH_SHORT).show()




                        }.addOnFailureListener {

                            Toast.makeText(this,"img not fetch",Toast.LENGTH_SHORT).show()

                        }


                    }.addOnFailureListener{

                        Toast.makeText(this,"img not upload",Toast.LENGTH_SHORT).show()

                    }

                }
            }


        }

        edtPhotoBtn.setOnClickListener {

            var i = Intent()
            i.action = Intent.ACTION_PICK
            i.type = "image/*"
            imageLauncher.launch(i)




        }

        backImgBtn.setOnClickListener {

            super.onBackPressed()

        }



    }
}
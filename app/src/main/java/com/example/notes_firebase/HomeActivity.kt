package com.example.notes_firebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar:android.widget.Toolbar
    lateinit var addBtn:ImageView
    lateinit var rootLayout:ConstraintLayout

    lateinit var rv: RecyclerView
    lateinit var pb: ProgressBar
    lateinit var searchView: SearchView

    lateinit var mList:ArrayList<Model>
    lateinit var adapter:RvAdapter
    lateinit var mReferernce: DatabaseReference

    private lateinit var auth : FirebaseAuth
  //  private lateinit var googleSignInClient : GoogleSignInClient
    lateinit var signOutBtn:TextView
    lateinit var nameTxt:TextView
    lateinit var email:String
    lateinit var displayName:String




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.option_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.optlogoutBtn->{

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                //  googleSignInClient = GoogleSignIn.getClient(this , gso)

                val go = GoogleSignIn.getClient(this , gso)
                go.signOut()

                auth.signOut()
                var i = Intent(this,LoginActivity::class.java)
                startActivity(i)
                finish()

            }

            R.id.optSettingBtn->{

                 startActivity(Intent(this,ProfileActivity::class.java))
            }


        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

      //  window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        auth = FirebaseAuth.getInstance()

      toolbar = findViewById(R.id.toolBar)
      setActionBar(toolbar)
        toolbar.inflateMenu(R.menu.option_menu)
        toolbar.setTitle("hy")


        rootLayout = findViewById(R.id.rootLayout)
        addBtn = findViewById(R.id.addBtn)
        signOutBtn = findViewById(R.id.signOutBtn)
        nameTxt = findViewById(R.id.nameTxt)

       email = intent.getStringExtra("email").toString()
        displayName = intent.getStringExtra("name").toString()

       // nameTxt.text = email?.replace(".","").toString()
       // nameTxt.text = email





        signOutBtn.setOnClickListener {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

          //  googleSignInClient = GoogleSignIn.getClient(this , gso)

          val go = GoogleSignIn.getClient(this , gso)
            go.signOut()

            auth.signOut()
            var i = Intent(this,LoginActivity::class.java)
            startActivity(i)
            finish()

        }

        addBtn.setOnClickListener {

                var i = Intent(this,AddNotesActivity::class.java)
            intent.putExtra("email" , email)
                startActivity(i)
         //   finish()

        }


        rv = findViewById(R.id.rv)
        pb = findViewById(R.id.pb)
        searchView = findViewById(R.id.searchView)

        rv.setHasFixedSize(true)
       // rv.layoutManager = GridLayoutManager(this,2)
        rv.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)

        rv.visibility = View.GONE
        pb.visibility = View.VISIBLE

        mList = ArrayList<Model>()

        mReferernce = FirebaseDatabase.getInstance().getReference("Email")


//        if (email != null) {
//            mReferernce = FirebaseDatabase.getInstance().getReference(email.replace(".","").toString())
//        }


//        if (email != null) {
//            mReferernce = FirebaseDatabase.getInstance().getReference(email.toString())
//        }

        mReferernce.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                mList.clear()
                if (snapshot.exists()){


                    for (user in snapshot.child(auth.currentUser!!.uid).child("Notes").children){

                        var data = user.getValue(Model::class.java)

                        mList.add(data!!)



                    }

                    adapter = RvAdapter(mList,this@HomeActivity)

                    rv.adapter = adapter

                    rv.visibility = View.VISIBLE
                    pb.visibility = View.GONE

                }



            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@HomeActivity,"Error", Toast.LENGTH_SHORT).show()
                rv.visibility = View.VISIBLE
                pb.visibility = View.GONE

            }


        })

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

               // searchFun(p0)
               return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                searchFun(p0)

                return true
            }
        })




    }

    private fun searchFun(p0: String?) {


        var searchList = ArrayList<Model>()
        for (i in mList){

            if (p0 != null) {
                if (i.name?.toLowerCase()?.contains(p0.toLowerCase()) == true || i.email?.toLowerCase()?.contains(p0.toLowerCase())  == true){

                    searchList.add(i)

                }
            }


        }
55
        adapter.searchListFun(searchList)

    }

    override fun onBackPressed() {
//        if(!searchView.isIconified()){
//
//            searchView.onActionViewCollapsed()
//        }else{
//            moveTaskToBack(true)
//
//        }
        moveTaskToBack(true)
    }
//    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
//
//
//        return super.getOnBackInvokedDispatcher()
//    }

//    override fun onResume() {
//        super.onResume()
//
//        searchView.setQuery("",false)
//        rootLayout.requestFocus()
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

       // searchView.onActionViewCollapsed()
        return super.onKeyDown(keyCode, event)
    }

}
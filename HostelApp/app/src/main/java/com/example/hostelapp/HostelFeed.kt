package com.example.hostelapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_hostel_feed.*

class HostelFeed : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    var userEmailFromFB : ArrayList<String> = ArrayList()
    var userCommentFromFB : ArrayList<String> = ArrayList()
    var userImageFromFB : ArrayList<String> = ArrayList()
    var hostelIDFromFB : ArrayList<String> = ArrayList()


    var adapter : FeedRecyclerAdapter? = null



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.custom_options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post){
            val hostelNameFromHostelActivity = intent.getStringExtra("hostelid")
            val intent = Intent(applicationContext,UploadActivity::class.java)
            println(hostelNameFromHostelActivity+"hostelfeed on optionitemselected")
            intent.putExtra("hostelid",hostelNameFromHostelActivity)
            startActivity(intent)
            finish()
        }else if(item.itemId == R.id.logout){
            auth.signOut()
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hostel_feed)

        auth = Firebase.auth

        db = FirebaseFirestore.getInstance()



        getDataFromFirestore()



        //recyclerview

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = FeedRecyclerAdapter(userEmailFromFB,userCommentFromFB,userImageFromFB)
        recyclerView.adapter = adapter
    }





    fun getDataFromFirestore() {


        db.collection("Posts").orderBy("date",
            Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            } else {

                if (snapshot != null) {
                    if (!snapshot.isEmpty) {

                        userImageFromFB.clear()
                        userCommentFromFB.clear()
                        userEmailFromFB.clear()
                        hostelIDFromFB.clear()

                        val documents = snapshot.documents
                        val hostelName = intent.getStringExtra("name")

                        for (document in documents) {
//                            val id = document.get("id") as String
//                            println(id+"this is the id comes from FB")
                            val comment = document.get("comment") as String
                            val useremail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val timestamp = document.get("date") as Timestamp
                            val date = timestamp.toDate()

                            userEmailFromFB.add(useremail)
                            userCommentFromFB.add(comment)
                            userImageFromFB.add(downloadUrl)

                            adapter!!.notifyDataSetChanged()

                        }


                    }
                }

            }
        }

    }

}
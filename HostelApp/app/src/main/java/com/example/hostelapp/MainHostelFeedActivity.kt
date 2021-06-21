package com.example.hostelapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_hostel_feed.*

class MainHostelFeedActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    var hostelNameFromFB : ArrayList<String> = ArrayList()
    var hostelInfoFromFB : ArrayList<String> = ArrayList()
    var userImageFromFB : ArrayList<String> = ArrayList()



    var adapter : HostelMainFeedRecyclerAdapter? = null


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.hostelfeed_options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_hostel){
            val intent = Intent(applicationContext,UploadHostelActivity::class.java)
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

    fun seeCommentsClicked(view: View){
        val intent = Intent(this,HostelFeed::class.java)
        intent.putExtra("hostelname",hostelNameFromFB)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hostel_feed)

        auth = Firebase.auth

        db = FirebaseFirestore.getInstance()

        getDataFromFirestore()


        //recyclerview

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        adapter = HostelMainFeedRecyclerAdapter(hostelNameFromFB,hostelInfoFromFB,userImageFromFB)
        recyclerView.adapter = adapter
    }






    fun getDataFromFirestore() {


        db.collection("Hostels").orderBy("date",
            Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            } else {

                if (snapshot != null) {
                    if (!snapshot.isEmpty) {

                        userImageFromFB.clear()
                        hostelInfoFromFB.clear()
                        hostelNameFromFB.clear()

                        val documents = snapshot.documents
                        for (document in documents) {
                            val info = document.get("information") as String
                            val hostelName = document.get("hostelName") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val timestamp = document.get("date") as Timestamp
                            val date = timestamp.toDate()

                            hostelNameFromFB.add(hostelName)
                            hostelInfoFromFB.add(info)
                            userImageFromFB.add(downloadUrl)

                            adapter!!.notifyDataSetChanged()

                        }


                    }
                }

            }
        }

    }


}
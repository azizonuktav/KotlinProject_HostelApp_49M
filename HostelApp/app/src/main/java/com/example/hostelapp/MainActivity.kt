package com.example.hostelapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        println("hello")
        if(currentUser != null){
            val intent = Intent(this,MainHostelFeedActivity::class.java)
            startActivity(intent)

            finish()
        }

    }

    fun signInClicked(view: View){
        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                    val intent = Intent(this,MainHostelFeedActivity::class.java)
                    startActivity(intent)
                    finish()
            }.addOnFailureListener {
                exception ->
                Toast.makeText(this,"Give your e-mail and password",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signUpClicked(view:View){
        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    //my user created, go to feed activity
                    val intent = Intent(this,MainHostelFeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }.addOnFailureListener {
                    exception ->
                Toast.makeText(this@MainActivity,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@MainActivity,"Give your e-mail and password",Toast.LENGTH_LONG).show()
        }




    }


}
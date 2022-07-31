package com.internshala.bookhub.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.internshala.bookhub.R

class LoginActivity : AppCompatActivity() { //, View.OnClickListener {

    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView
    private val validMobileNumber = "0123456789"
    private val validPassword = "Gullu"
    lateinit var toolbar: Toolbar

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                                                     // USE OF SHARED PREFERENCES !!

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
        setContentView(R.layout.activity_login)

        if(isLoggedIn){
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //title = "Log In" // TITLE PAGE

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)

                                                         // SET FOR TOOLBAR !!
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Log In"  // Till here..

                                                                    // SET OnCLICK LISTENER METHOD!!
        btnLogin.setOnClickListener {

            val mobileNumber = etMobileNumber.text.toString()

            val password = etPassword.text.toString()

            if((mobileNumber == validMobileNumber) && (validPassword.contains(password))){
                  val intent = Intent(this@LoginActivity, MainActivity::class.java)
                  savePreferences()
                  startActivity(intent)

              } else {
                Toast.makeText(this@LoginActivity, "Incorrect Credentials!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    // SET onPause() method to  "CLOSE OUR APP" when we go back from our Avengers page !!
    override fun onPause(){
        super.onPause()
        finish()
    }

    // Use Of SAVE PREFRENCES !!
    fun savePreferences() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
}

package com.example.entryticketsales

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.entryticketsales.utils.isValidEmail
import com.example.entryticketsales.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnNext.setOnClickListener {

            val userName = etUserName.text.toString()
            val email    = etEmail.text.toString()

            if(userName.isEmpty())
                etUserName.error = "Please fill user name first!"

            if(email.isEmpty())
                etEmail.error = "Enter your email!"

            else if (userName.isNotEmpty() && email.isNotEmpty()){

                if(!isValidEmail(email))
                    showToast(
                        this,
                        resources.getString(R.string.invalid_email)
                    )

                else{
                    val settings = getSharedPreferences("PREFS", 0)
                    val editor = settings.edit()
                    editor.putString("userName", userName)
                    editor.putString("email",email)
                    editor.apply()

                    startActivity(MainActivity.getIntent(this))
                    finish()
                }
            }
        }
    }
}

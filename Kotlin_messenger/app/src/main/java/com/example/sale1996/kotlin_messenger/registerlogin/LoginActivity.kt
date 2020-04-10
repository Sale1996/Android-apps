package com.example.sale1996.kotlin_messenger.registerlogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.kotlin_messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            //ovako se vrsi logovanje preko Firebase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener()
//                .a
        }

        back_to_register_login.setOnClickListener {
            finish()
            //ovo finish samo vraca intent nazad
        }
    }
}
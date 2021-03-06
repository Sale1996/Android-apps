package com.example.sale1996.kotlin_messenger.registerlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.kotlin_messenger.R
import com.example.sale1996.kotlin_messenger.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
/*
* intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
*
* Ova linija nam govori ukoliko startujemo novu aktivnost preko Intenta, sa ovim flagovima
* govorimo da stack aktivnosti krecemo od te aktivnosti i ako probamo back dugme na toj
* aktivnosti mi se necemo vracati na login screen.
* */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            // Ovako se vrsi logovanje preko Firebase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
                }

        }

        back_to_register_login.setOnClickListener {
            finish() //ovo finish samo vraca intent nazad, odnosno vraca na register screen, jer samo tako mozemo doci do ovog prozora
        }
    }
}
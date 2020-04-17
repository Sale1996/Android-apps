package com.example.sale1996.fire_message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

/*
* Ova aktivnost ide skroz na pocetku i proverava da li je korisnik vec ulogovan,
* ako jeste onda skacemo na pocetni ekran, ako nije onda skacemo na login screen
*
* */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null){
            //funkcija od anko
            startActivity<SignInActivity>()
        }
        else{
            startActivity<MainActivity>()
        }
        finish()
    }
}
package com.example.sale1996.fire_message.service
import android.util.Log
import com.example.sale1996.fire_message.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

/*
* Firebase cuva TOKEN za svaki uredjaj, i mi moramo da upamtimo koji
* korisnik je vezan za koji telefon tako sto cemo sacuvati u firestore database
*
* I onda kada posaljemo nekome poruku, on ce preko id od naseg chatpartnera naci sve tokene
* i poslati svim njegovim uredjajima obavestenje za poruku...
*
* */
class MyFirebaseInstanceIDService : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val newRegistrationToken = p0

        //sada svaki put kad se refresha token mi ovde pozivamo onNewToken funkciju...
        //ali tu postoji problem, jer korisnik ne mora biti ulogovan kada se ova funkcija porkene..
        //ona moze da se pokrene i prvi put kada je instalirana aplikacija....
        if (FirebaseAuth.getInstance().currentUser != null)
            addTokenToFirestore(newRegistrationToken)


    }

    companion object {
        /*
        * Jedan nalog moze da bude logovan na vise uredjaja, stoga je tu postoji
        * povratna vrednost od liste tokena.
        * */
        fun addTokenToFirestore(newRegistrationToken: String?){
            if(newRegistrationToken == null) throw NullPointerException("FCM token is null.")

             FirestoreUtil.getFCMRegistrationTokens { tokens ->
                 if(tokens.contains(newRegistrationToken)) return@getFCMRegistrationTokens

                 tokens.add(newRegistrationToken)
                 FirestoreUtil.setFCMRegistrationTokens(tokens)
             }
        }
    }





}
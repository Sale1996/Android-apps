package com.example.sale1996.fire_message

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sale1996.fire_message.service.MyFirebaseInstanceIDService
import com.example.sale1996.fire_message.util.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask


class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1

    // pravimo kako hocemo da nam izgleda UI od firebase
    private val signInProviders =
        listOf(AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true)
            .build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // ovde pokrecemo taj UI od firebase
        account_sign_in.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setLogo(R.drawable.ic_fire_emoji)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Setting up your account")

                //E sada cemo da pozovemo metodu UTIL-a koji smo napravili i kreirati usera u
                //firebase datasetu
                FirestoreUtil.initCurrentUserIfFirstTime {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())

                    //ovde sada kupimo token i prosledjujemo ga u firebase!
                    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
                        this,
                        OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                            val userToken = instanceIdResult.token
                            MyFirebaseInstanceIDService.addTokenToFirestore(userToken)
                        })
                    progressDialog.dismiss()
                }


            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        longSnackbar(constraint_layout_sign_in, "No network!")
                    ErrorCodes.UNKNOWN_ERROR ->
                        longSnackbar(constraint_layout_sign_in, "Unknown error")
                }
            }
        }
    }
}
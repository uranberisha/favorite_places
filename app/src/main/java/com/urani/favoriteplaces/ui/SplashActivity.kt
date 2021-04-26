package com.urani.favoriteplaces.ui

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.ui.auth.LoginActivity
import com.urani.favoriteplaces.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    //Firebase
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFirebaseAuth()
    }


    private fun setupFirebaseAuth(){
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null && user.isEmailVerified) {
                // User is authenticated
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("user_id", user.uid)
                intent.putExtra("email", user.email)
                startActivity(intent)
            } else {
                // User is not logged in
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (::mAuthListener.isInitialized) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener)
        }
    }

    override fun onStop() {
        super.onStop()
        if (::mAuthListener.isInitialized) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val a = "test"
    }
}
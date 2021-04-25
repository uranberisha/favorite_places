package com.urani.favoriteplaces.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.urani.favoriteplaces.ui.main.MainActivity
import com.urani.favoriteplaces.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    //Firebase
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is authenticated
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("user_id", user.uid)
                intent.putExtra("email", user.email)
                startActivity(intent)
            } else {
                // User is not logged in
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            //
        }
        return super.onCreateView(name, context, attrs)
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener)
        }
    }
}
package com.urani.favoriteplaces

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.urani.favoriteplaces.databinding.ActivityMainBinding
import com.urani.favoriteplaces.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val userId = intent.getStringExtra("user_id")
        val email = intent.getStringExtra("email")

        supportFragmentManager.beginTransaction().add(R.id.fragment_container_layout, MainFragment())
            .commit()
    }

}
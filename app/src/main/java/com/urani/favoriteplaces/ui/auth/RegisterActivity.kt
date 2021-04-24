package com.urani.favoriteplaces.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.urani.favoriteplaces.MainActivity
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.databinding.ActivityRegisterBinding
import com.urani.favoriteplaces.extension.*
import com.urani.favoriteplaces.models.User
import com.urani.favoriteplaces.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.firstNameEditText.textPersonName()
        binding.secondNameEditText.textPersonName()

        mAuth = FirebaseAuth.getInstance()

    }

    fun onBackClickButton(view: View) {
        Utils.hideSoftKeyBoard(view, this)
        onBackPressed()
    }

    fun onRegisterButtonClick(view: View) {
        if (validateFields()) {
            binding.btnRegister.isEnabled = false

            binding.progressBar.visible()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()

            mAuth.createUserWithEmailAndPassword(
                    email,
                    password
            )
                    .addOnCompleteListener { task ->
                        binding.progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            toast("created account successfully !")
                            addNewUser()

                        } else {
                            toast("failed to Authenticate !")
                        }
                    }


        } else {
            binding.btnRegister.isEnabled = true
        }
    }


    private fun addNewUser() {

        binding.btnRegister.isEnabled = true
        //add data to the "users" node
        val userId = FirebaseAuth.getInstance().currentUser.uid
        val mUser = User(binding.firstNameEditText.text.toString().trim(),
                binding.secondNameEditText.text.toString().trim(),
                userId)

        database = Firebase.database.reference

        //insert into users node
        val email = binding.emailEditText.text.toString().trim()
        database.child(getString(R.string.node_users))
                .child(userId)
                .setValue(mUser)
                .addOnSuccessListener {
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("user_id", userId)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    val a = it
                }

    }


    private fun validateFields(): Boolean {
        val errorArray: MutableList<String> = ArrayList()

        if (!Utils.validateField(binding.firstNameEditText)) {
            errorArray.add(getString(R.string.please_fill_first_name))
        }

        if (!Utils.validateField(binding.secondNameEditText)) {
            errorArray.add(getString(R.string.please_fill_last_name))
        }

        if (!Utils.validateField(binding.emailEditText)) {
            errorArray.add(getString(R.string.please_fill_email))
        } else if (!binding.emailEditText.text.toString().isEmailValid()) {
            Utils.showErrorEditText(binding.emailEditText)
            errorArray.add(getString(R.string.invalid_email_message))
        }


        when {
            Utils.validateEmptyField(binding.passwordEditText) -> {
                Utils.showErrorEditText(binding.passwordEditText)
                errorArray.add(getString(R.string.please_fill_password))
            }
            !Utils.validatePassword(binding.passwordEditText.text.toString()) -> {
                Utils.showErrorEditText(binding.passwordEditText)
                errorArray.add(getString(R.string.invalid_password_message))
            }
        }

        when {
            errorArray.size > 1 -> {
                binding.textErrorOnRegister.text = getString(R.string.error_in_register)
                binding.textErrorOnRegister.visible()
            }
            errorArray.size == 1 -> {
                binding.textErrorOnRegister.text = errorArray[0]
                binding.textErrorOnRegister.visible()
            }
            else -> {
                binding.textErrorOnRegister.gone()
            }
        }

        return errorArray.isEmpty()
    }
}
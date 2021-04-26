package com.urani.favoriteplaces.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.databinding.ActivityRegisterBinding
import com.urani.favoriteplaces.extension.gone
import com.urani.favoriteplaces.extension.isEmailValid
import com.urani.favoriteplaces.extension.toast
import com.urani.favoriteplaces.extension.visible
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
                        task.result?.user?.let {
                            //addNewUser()
                            sendVerificationEmail(it)
                        }

                    } else {
                        toast(getString(R.string.failed_to_authenticate))
                    }
                }


        } else {
            binding.btnRegister.isEnabled = true
        }
    }


//    private fun addNewUser() {
//        val userId = FirebaseAuth.getInstance().currentUser.uid
//        val mUser = User(
//            binding.firstNameEditText.text.toString().trim(),
//            binding.secondNameEditText.text.toString().trim(),
//            userId
//        )
//
//        database = Firebase.database.reference
//
//        //insert into users node
//        val email = binding.emailEditText.text.toString().trim()
//        database.child(getString(R.string.node_users))
//            .child(userId)
//            .setValue(mUser)
//            .addOnSuccessListener {
//                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                intent.putExtra("user_id", userId)
//                intent.putExtra("email", email)
//                startActivity(intent)
//                finish()
//            }
//            .addOnFailureListener {
//            }
//
//    }

    private fun sendVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toast(getString(R.string.confirm_link_was_sent_message))
                FirebaseAuth.getInstance().signOut();
                onBackClickButton(binding.root)
                //finish()
            } else {
                toast(getString(R.string.could_not_sent_email))
            }
        }
    }


    private fun validateFields(): Boolean {
        val errorArray: MutableList<String> = ArrayList()

//        if (!Utils.validateField(binding.firstNameEditText)) {
//            errorArray.add(getString(R.string.please_fill_first_name))
//        }
//
//        if (!Utils.validateField(binding.secondNameEditText)) {
//            errorArray.add(getString(R.string.please_fill_last_name))
//        }

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
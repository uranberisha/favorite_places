package com.urani.favoriteplaces.ui.auth

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.urani.favoriteplaces.ui.main.MainActivity
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.databinding.ActivityLoginBinding
import com.urani.favoriteplaces.extension.gone
import com.urani.favoriteplaces.extension.isEmailValid
import com.urani.favoriteplaces.extension.toast
import com.urani.favoriteplaces.extension.visible
import com.urani.favoriteplaces.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        binding.registerTxtView.paintFlags =
            binding.registerTxtView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }


    fun onLoginButtonClick(view: View) {
        if (validateFields()){
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            binding.progressBar.visible()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        task.result?.user?.let {
                            val firebaseUser  = it
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }

                    } else {
                        toast("failed to Authenticate !")
                    }
                }
        }
    }
    fun onRegisterButtonClick(view: View) {
        clearFields()
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun clearFields() {
        binding.emailEditText.text?.clear()
        Utils.hideErrorEditText(binding.emailEditText)
        binding.passwordEditText.text?.clear()
        Utils.hideErrorEditText(binding.passwordEditText)
        binding.textErrorOnLogin.gone()
    }

    private fun validateFields(): Boolean {
        val errorArray: MutableList<String> = ArrayList()

        when {
            !Utils.validateField(binding.emailEditText) -> {
                Utils.showErrorEditText(binding.emailEditText)
                errorArray.add(getString(R.string.please_fill_email))
            }
            !binding.emailEditText.text.toString().isEmailValid() -> {
                Utils.showErrorEditText(binding.emailEditText)
                errorArray.add(getString(R.string.please_fill_email))

            }
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
                binding.textErrorOnLogin.text = getString(R.string.error_in_login)
                binding.textErrorOnLogin.visible()
            }
            errorArray.size == 1 -> {
                binding.textErrorOnLogin.text = errorArray[0]
                binding.textErrorOnLogin.visible()
            }
            else -> {
                binding.textErrorOnLogin.gone()
            }
        }

        return errorArray.isEmpty()
    }

}
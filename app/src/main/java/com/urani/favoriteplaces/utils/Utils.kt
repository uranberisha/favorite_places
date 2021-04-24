package com.urani.favoriteplaces.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.extension.clearError
import java.util.regex.Pattern

object Utils {

    private val VALID_PASSWORD_REGEX = Pattern.compile(
        "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})",
        Pattern.CASE_INSENSITIVE
    )

    fun validateEmptyField(editText: EditText): Boolean {
        return editText.text.toString().trim { it <= ' ' } == ""
    }

    fun validateField(
        editText: EditText
    ): Boolean {
        return if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            showErrorEditText(editText, "")
            false
        } else {
            hideErrorEditText(editText)
            true
        }
    }

    fun validatePassword(password: String): Boolean {
        val matcher = VALID_PASSWORD_REGEX.matcher(password)
        return matcher.find()
    }

    fun showErrorEditText(
        editText: EditText,
        message: String = ""
    ) {
        if (message.isNotEmpty()) {
            editText.error = message
        }
            editText.setBackgroundResource(R.drawable.rounded_background_white_1dp_with_yellow_borders)
    }

    fun hideErrorEditText(
        editText: EditText
    ) {
        editText.clearError()
        editText.setBackgroundResource(R.drawable.rounded_background_white_1dp_with_grey_borders)
    }


    fun hideSoftKeyBoard(view: View, context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
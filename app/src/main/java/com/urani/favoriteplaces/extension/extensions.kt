package com.urani.favoriteplaces.extension

import android.content.Context
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast

fun Context.toast(message: String, length: Int= Toast.LENGTH_SHORT) = Toast.makeText(this, message, length).show()

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun EditText.clearError() {
    error = null
}

fun EditText.textPersonName() {
    this.filters = arrayOf(
        InputFilter { cs, _, _, _, _, _ ->
            if (cs == "") { // for backspace
                return@InputFilter cs
            }
            if (cs.toString()
                    .matches(Regex("[a-zA-ZàâäôéèëêïîçùûüÿæœÀÂÄÔÉÈËÊÏÎŸÇÙÛÜÆŒößÖẞ ]+"))
            ) {
                cs
            } else ""
        }, InputFilter.LengthFilter(50)
    )
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


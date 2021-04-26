package com.urani.favoriteplaces.customDialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.databinding.DialogConfirmBinding
import com.urani.favoriteplaces.extension.visible

class ConfirmDialog (
    private val mContext: Context,
    private val title: String? = null,
    private val description: String? = null,
    private val confirmCallback: () -> Unit
) : Dialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: DialogConfirmBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_confirm,
            null,
            false
        )
        setContentView(binding.root)

        window!!.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.transparent
                )
            )
        )

        if (title != null)
            binding.txtTitle.text = title


        if (description != null){
            binding.txtDescription.text = description
            binding.txtDescription.visible()
        }

        binding.btnNo.setOnClickListener {
            dismiss()
        }

        binding.btnYes.setOnClickListener {
            confirmCallback.invoke()
            dismiss()
        }

    }
}
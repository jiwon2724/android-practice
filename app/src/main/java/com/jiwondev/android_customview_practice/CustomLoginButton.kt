package com.jiwondev.android_customview_practice

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.jiwondev.android_customview_practice.databinding.CustomLoginButtonBinding

class CustomLoginButton constructor(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {
    private val binding: CustomLoginButtonBinding = CustomLoginButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomLoginButton)

        val text = typedArray.getString(R.styleable.CustomLoginButton_text)
        binding.title.text = text

        val symbol = typedArray.getResourceId(R.styleable.CustomLoginButton_symbol, R.drawable.ic_launcher_foreground)
        Glide.with(this).load(symbol).into(binding.symbol)

        typedArray.recycle()
    }
}
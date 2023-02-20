package com.jiwondev.android_customview_practice

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.jiwondev.android_customview_practice.databinding.CustomLoginButtonBinding

val TAG = "CustomLoginButton"

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure")
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d("widthMode : ", widthMode.toString())
        Log.d("widthSize : ", widthSize.toString())
        Log.d("heightMode : ", heightMode.toString())
        Log.d("heightSize : ", heightSize.toString())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("left : ", left.toString())
        Log.d("right : ", right.toString())
        Log.d("top : ", top.toString())
        Log.d("bottom : ", bottom.toString())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "onKeyDown")
        return super.onKeyDown(keyCode, event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.d(TAG, "onFinishInflate")
    }
}
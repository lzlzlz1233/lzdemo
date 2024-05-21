package com.example.nfonsite.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nfonsite.databinding.ErrorLoadingViewBinding

/**
 * Error View to handle basic errors
 */
class ErrorLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding: ErrorLoadingViewBinding =
        ErrorLoadingViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setUp(errorText: String, onClick: () -> Unit) = with(binding) {
        this.errorText.text = errorText
        this.button.setOnClickListener { onClick() }
    }
}
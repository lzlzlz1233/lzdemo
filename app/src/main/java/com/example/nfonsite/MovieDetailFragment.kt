package com.example.nfonsite

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.nfonsite.databinding.DialogFragmentBinding

/**
 * Simple Dialog Fragment to show overlay when clicking the grid
 */
class MovieDetailFragment : DialogFragment() {

    private var _binding: DialogFragmentBinding? = null
    val binding: DialogFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val img = arguments?.getString(IMAGE_KEY)
        val name = arguments?.getString(NAME_KEY)
        val overview = arguments?.getString(OVER_VIEW_KEY)

        _binding = DialogFragmentBinding.inflate(layoutInflater, container, false)
        setUpStyle()
        setUp(img, name, overview)
        binding.close.setOnClickListener {
            this.dismiss()
        }

        return binding.root
    }

    private fun setUpStyle(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    private fun setUp(img: String?, name : String?, overview: String?){
        binding.title.text = name
        binding.overView.text = overview
        val width = resources.displayMetrics.widthPixels
        context?.let {
            Glide.with(it)
                .load(img)
                .placeholder(R.drawable.ic_launcher_foreground)
                .override(width,(width * 0.75).toInt())
                .into(binding.image)
        }
    }

    companion object{
        val FRAG_TAG = "diaglog_frag"
        val IMAGE_KEY = "image_key"
        val NAME_KEY = "name_key"
        val OVER_VIEW_KEY = "overview_key"
    }
}
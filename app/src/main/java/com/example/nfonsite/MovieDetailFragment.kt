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
import com.example.nfonsite.uiModel.FeedItem

class MovieDetailFragment : DialogFragment() {

    private var _binding: DialogFragmentBinding? = null
    val binding: DialogFragmentBinding get() = _binding!!

    private var item: FeedItem.MovieItem? = null

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
        return binding.root
    }

    fun setUpStyle(){
        getDialog()?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }
    
    fun setUp(img: String?, name : String?, overview: String?){
        binding.title.text = img
        binding.overView.text = overview
        context?.let {
            Glide.with(it)
                .load(img)
                .placeholder(R.drawable.ic_launcher_background)
                .override(350,500)
                .into(binding.image)
        }
    }

    companion object{
        val FRAG_TAG = "diaglog_frag"
        val IMAGE_KEY = "image_key"
        val NAME_KEY = "image_key"
        val OVER_VIEW_KEY = "overview_key"
    }
}
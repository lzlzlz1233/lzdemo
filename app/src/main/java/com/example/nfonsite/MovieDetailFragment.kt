package com.example.nfonsite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.nfonsite.databinding.DialogFragmentBinding

class MovieDetailFragment : DialogFragment() {

    private var _binding: DialogFragmentBinding? = null
    val binding: DialogFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}
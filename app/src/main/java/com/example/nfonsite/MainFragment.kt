package com.example.nfonsite

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.core.view.marginEnd
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.ErrorType
import com.example.nfonsite.MovieDetailFragment.Companion.FRAG_TAG
import com.example.nfonsite.MovieDetailFragment.Companion.IMAGE_KEY
import com.example.nfonsite.MovieDetailFragment.Companion.NAME_KEY
import com.example.nfonsite.MovieDetailFragment.Companion.OVER_VIEW_KEY
import com.example.nfonsite.databinding.MainFragmentBinding
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.uiModel.MovieItemSpec
import com.example.nfonsite.util.MovieAdapter
import com.example.nfonsite.util.UiState

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MainFragment : Fragment() {

    private val SPAN_COUNT  = 3
    private var _binding: MainFragmentBinding? = null
    val binding: MainFragmentBinding get() = _binding!!

    private var adapter: MovieAdapter?= null

    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = this.context?.let { MovieAdapter(it, this:: openMovieDetail) }
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        binding.recycler.setPadding(0,0,0,0)
        viewModel.data.observe(viewLifecycleOwner, this::updateUi)
        viewModel.getList()
        setUpSearchBar()
        binding.search.setOnClickListener {

        }
    }

    private fun setUpSearchBar(){
        binding.search.setOnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER){
                val text = binding.search.getText().toString()
                viewModel.search(text)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun openMovieDetail(item : FeedItem){

        val fragment = MovieDetailFragment()
        fragment.arguments = Bundle().apply {
            val movie = (item as FeedItem.MovieItem).movie
            this.putString(NAME_KEY, movie.id)
            this.putString(IMAGE_KEY, "https://image.tmdb.org/t/p/w500" + movie.imgPath)
            this.putString(OVER_VIEW_KEY, movie.overView)
        }
        activity?.supportFragmentManager?.beginTransaction()?.add(fragment,FRAG_TAG)?.commitNowAllowingStateLoss()
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun updateUi(state: UiState<MovieViewModel.ScreenContent>) {
        when (state) {
            is UiState.Error -> {
                binding.swipeLayout.isRefreshing = false
                when (state.errorMessage) {
                    ErrorType.OTHER.name -> setUpErrorView(R.string.default_mssage)
                    ErrorType.IO.name -> setUpErrorView(R.string.io_exception)
                    ErrorType.HTTP.name -> setUpErrorView(R.string.http_exception)
                    ErrorType.MALFORMED.name -> setUpErrorView(R.string.data_exception)
                    else -> setUpErrorView(R.string.default_mssage)
                }
            }

            is UiState.Success -> {
                binding.swipeLayout.isRefreshing = false
                if (state.data.items.isEmpty()) {
                    setUpErrorView(R.string.no_found)
                }
                adapter?.updateList(state.data.items)
                hideErrorView()
            }

            is UiState.Loading -> {
                binding.swipeLayout.isRefreshing = true
                hideErrorView()
            }

            is UiState.Empty -> {
                hideErrorView()
            }
        }
    }

    private fun hideErrorView(){
        binding.errorView.visibility = GONE
    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun setUpErrorView(res: Int) {
        context?.getString(res)
            ?.let { binding.errorView.setUp(it, this::onReloadClick) }
        binding.errorView.visibility = VISIBLE
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun onReloadClick() {
//        viewModel.refresh()
    }


}
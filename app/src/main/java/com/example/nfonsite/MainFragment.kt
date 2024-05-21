package com.example.nfonsite

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.ErrorType
import com.example.nfonsite.MovieDetailFragment.Companion.FRAG_TAG
import com.example.nfonsite.MovieDetailFragment.Companion.IMAGE_KEY
import com.example.nfonsite.MovieDetailFragment.Companion.NAME_KEY
import com.example.nfonsite.MovieDetailFragment.Companion.OVER_VIEW_KEY
import com.example.nfonsite.databinding.MainFragmentBinding
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.util.MovieAdapter
import com.example.nfonsite.util.UiState

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MainFragment : Fragment() {

    private val PORTRIAT_SPAN_COUNT  = 3
    private val LANDSCAPE_SPAN_COUNT  = 6

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
        binding.search.clearFocus()
        if (adapter == null){
            adapter = this.context?.let { MovieAdapter(it, this::openMovieDetail) }
        }
        binding.recycler.adapter = adapter
        setUpSpan()
        binding.recycler.setPadding(0, 0, 0, 0)
        viewModel.data.observe(viewLifecycleOwner, this::updateUi)
        viewModel.getList()
        setUpSearchBar()

        binding.swipeLayout.apply {
            this.setOnRefreshListener {
                binding.search.clearFocus()
                binding.search.text.clear()
                adapter?.updateList(emptyList())
                viewModel.refresh()
            }
        }

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && viewModel.canLoadMore()){
                    viewModel.loadMore()
                }
            }
        })
    }

    private fun setUpSpan(){
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            binding.recycler.layoutManager = GridLayoutManager(context, LANDSCAPE_SPAN_COUNT)
        } else {
            // In portrait
            binding.recycler.layoutManager = GridLayoutManager(context, PORTRIAT_SPAN_COUNT)

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
            this.putString(NAME_KEY, movie.name)
            this.putString(IMAGE_KEY, "https://image.tmdb.org/t/p/w500" + movie.imgPath)
            this.putString(OVER_VIEW_KEY, movie.overView)
        }
        activity?.supportFragmentManager?.beginTransaction()?.add(fragment,FRAG_TAG)?.commitNowAllowingStateLoss()
    }

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
                binding.search.setText(state.data.query)
                binding.swipeLayout.isRefreshing = false
                if (state.data.items.isEmpty()) {
                    setUpErrorView(R.string.no_found)
                }else{
                    hideErrorView()
                }
                adapter?.updateList(state.data.items)
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
    private fun setUpErrorView(res: Int) {
        context?.getString(res)
            ?.let { binding.errorView.setUp(it, this::onReloadClick) }
        binding.errorView.visibility = VISIBLE
    }

    private fun onReloadClick() {
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.search.clearFocus()
    }






}
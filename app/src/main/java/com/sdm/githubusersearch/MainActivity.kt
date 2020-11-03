package com.sdm.githubusersearch

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sdm.githubusersearch.animation.ZoomThumb
import com.sdm.githubusersearch.model.SearchViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors
    @Inject
    lateinit var zoomThumb: ZoomThumb

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model: SearchViewModel by viewModels {
            viewModelFactory
        }
        searchViewModel = model

        initSearchInputListener()
        initRecyclerView()


    }

    private fun initSearchInputListener() {
        findViewById<EditText>(R.id.input).setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(view: TextView) {
        val query = view.text.toString()
        dismissKeyboard(view.windowToken)
        searchViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.user_list)
        val adapter = UserAdapter { _, view ->
            zoomThumb.zoomImageFromThumb(view, findViewById(R.id.expanded_image), view.drawable)
        }
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    searchViewModel.loadNextPage()
                }
            }
        })
        searchViewModel.results.observe(this, Observer { result ->
            adapter.submitList(result?.data)
        })
        val loadMoreBar = findViewById<ProgressBar>(R.id.load_more_bar)

        searchViewModel.loadMoreStatus.observe(this, Observer { loadingMore ->
            if (loadingMore == null) {
                loadMoreBar.visibility = View.GONE
            } else {
                loadMoreBar.visibility = if (loadingMore.isRunning) View.VISIBLE else View.GONE
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }
}

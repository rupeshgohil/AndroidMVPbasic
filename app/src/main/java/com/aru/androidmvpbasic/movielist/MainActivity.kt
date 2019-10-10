package com.aru.androidmvpbasic.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aru.androidmvpbasic.adapter.MoviesAdapter
import com.aru.androidmvpbasic.listener.MutiClickListener
import com.aru.androidmvpbasic.modal.Movie
import com.aru.androidmvpbasic.modal.MovieListResponse
import com.aru.androidmvpbasic.movielist.presenter.MovieListview
import com.aru.androidmvpbasic.movielist.presenter.MovieListPresenter
import com.aru.androidmvpbasic.util.NetworkUtils.isNetworkAvailable
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.aru.androidmvpbasic.util.GridSpacingItemDecoration
import com.aru.androidmvpbasic.util.GridSpacingItemDecoration.dpToPx
import android.content.Intent
import com.aru.androidmvpbasic.moviefilter.MovieFilterActivity
import com.aru.androidmvpbasic.util.AppConstant.*
import com.aru.androidmvpbasic.util.AppConstant.KEY_RELEASE_TO
import com.aru.androidmvpbasic.util.AppConstant.KEY_RELEASE_FROM
import android.app.Activity
import com.aru.androidmvpbasic.util.AppConstant.ACTION_MOVIE_FILTER
import androidx.recyclerview.widget.RecyclerView
import com.aru.androidmvpbasic.R
import com.aru.androidmvpbasic.moviedetails.MovieDetailsActivity


class MainActivity : AppCompatActivity(),MovieListview.ShowEmptyView, MovieListview.View,
    MovieListview.MovieDataPresenter, MutiClickListener {

    var pageNo:Int = 1

    //Constants for load more
    var previousTotal:Int = 0
    var loading:Boolean= true
    var visibleThreshold:Int = 5
    var firstVisibleItem: Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var fromReleaseFilter = ""
    private var toReleaseFilter = ""
    private val TAG = "MovieListActivity"
    var mMoviewArrayList = ArrayList<Movie>()
    var mMovieListAdapter: MoviesAdapter?= null
    private var mLayoutManager: GridLayoutManager? = null
    private var movieListPresenter: MovieListPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
        loadMoredata()
        movieListPresenter  = MovieListPresenter(this)
        if(isNetworkAvailable(this)){
            movieListPresenter!!.requestMovieData(pageNo)
        }
        fab_filter.setOnClickListener {
            val movieFilterIntent = Intent(this@MainActivity, MovieFilterActivity::class.java)
            movieFilterIntent.putExtra(KEY_RELEASE_FROM, fromReleaseFilter)
            movieFilterIntent.putExtra(KEY_RELEASE_TO, toReleaseFilter)
            startActivityForResult(movieFilterIntent, ACTION_MOVIE_FILTER)
        }

    }

    private fun loadMoredata() {
        rv_movie_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = rv_movie_list.getChildCount()
                totalItemCount = mLayoutManager!!.getItemCount()
                firstVisibleItem = mLayoutManager!!.findFirstVisibleItemPosition()
                Log.e(TAG,"visibleItemCount"+visibleItemCount)
                Log.e(TAG,"totalItemCount"+totalItemCount)
                Log.e(TAG,"firstVisibleItem"+firstVisibleItem)

                // Handling the infinite scroll
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    if(isNetworkAvailable(this@MainActivity)){
                        Log.e(TAG,"Page_number"+pageNo)
                        movieListPresenter!!.requestMovieData(pageNo)
                    }
                    loading = true
                }

                // Hide and show Filter button
                if (dy > 0 && fab_filter.getVisibility() === View.VISIBLE) {
                    fab_filter.hide()
                } else if (dy < 0 && fab_filter.getVisibility() !== View.VISIBLE) {
                    fab_filter.show()
                }
            }
        })
    }

    private fun setView() {
        mLayoutManager = GridLayoutManager(this, 2)
        rv_movie_list.setLayoutManager(mLayoutManager)
        rv_movie_list.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(this, 10), true))
        rv_movie_list.setItemAnimator(DefaultItemAnimator())
        mMovieListAdapter = MoviesAdapter(this,mMoviewArrayList)
        rv_movie_list.adapter = mMovieListAdapter
    }

    override fun showEmptyView() {
        tv_empty_view.visibility = View.VISIBLE
        rv_movie_list.visibility = View.GONE
    }

    override fun hideEmptyView() {
        tv_empty_view.visibility = View.GONE
        rv_movie_list.visibility = View.VISIBLE
    }
    override fun showProgress() {
        pb_loading.visibility = View.VISIBLE
    }
    override fun hideProgress() {
        pb_loading.visibility = View.GONE
    }
    override fun setDtaToView(mModal: MovieListResponse) {
        Log.e(TAG,"Total_result"+mModal.totalResults)
        Log.e(TAG,"Total_Pange"+mModal.totalPages)

        mMoviewArrayList.addAll(mModal.results);
        Log.e(TAG,"Total_ResultSize"+mMoviewArrayList.size)
        mMovieListAdapter!!.notifyDataSetChanged();

        // This will help us to fetch data from next page no.
        pageNo++;
    }
    override fun onMultiClick(str: String, position: Int, mView: View) {
        if (position == -1) {
            return
        }
        Log.e(TAG,"MoviewId"+mMoviewArrayList.get(position).getId())
        val detailIntent = Intent(this, MovieDetailsActivity::class.java)
        detailIntent.putExtra(KEY_MOVIE_ID, mMoviewArrayList.get(position).getId().toString())
        startActivity(detailIntent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == ACTION_MOVIE_FILTER) {
            if (resultCode == Activity.RESULT_OK) {
                // Checking if there is any data to filter
                fromReleaseFilter = data!!.getStringExtra(KEY_RELEASE_FROM)
                toReleaseFilter = data.getStringExtra(KEY_RELEASE_TO)

                mMovieListAdapter!!.setFilterParameter(fromReleaseFilter, toReleaseFilter)
                mMovieListAdapter!!.getFilter().filter("")
            }
        }
    }

}

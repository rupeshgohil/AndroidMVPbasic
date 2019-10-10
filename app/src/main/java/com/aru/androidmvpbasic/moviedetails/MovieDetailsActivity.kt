package com.aru.androidmvpbasic.moviedetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aru.androidmvpbasic.BaseActivity
import com.aru.androidmvpbasic.R
import com.aru.androidmvpbasic.modal.Movie
import com.aru.androidmvpbasic.modal.MovieListResponse
import com.aru.androidmvpbasic.movielist.presenter.MovieListview
import com.aru.androidmvpbasic.util.AppConstant.KEY_MOVIE_ID
import com.aru.androidmvpbasic.util.NetworkUtils
import com.aru.androidmvpbasic.util.NetworkUtils.isNetworkAvailable
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.content_movie_details.*
import com.bumptech.glide.request.RequestOptions
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.aru.androidmvpbasic.Api.ApiConstant.BACKDROP_BASE_URL
import com.bumptech.glide.Glide

import androidx.annotation.Nullable
import com.aru.androidmvpbasic.adapter.CastAdapter
import com.aru.androidmvpbasic.modal.Cast
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout

import com.google.android.material.appbar.CollapsingToolbarLayout
import java.util.ArrayList

class MovieDetailsActivity : BaseActivity(), MovieListview.ShowEmptyView, MovieListview.View,
    MovieListview.MovieDetailsDataPresenter {



    var TAG:String="MovieDetailsActivity"
    var strMovie_id:String?= null
    var mMovieModal = Movie()
    var mCastArray = ArrayList<Cast>()
    private  var castAdapter: CastAdapter? =null
    private var movieListPresenter: MovieDetailsPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        intent = intent
        if(intent!= null){
            strMovie_id = intent.getStringExtra(KEY_MOVIE_ID)
            Log.e(TAG,"Movie_id"+strMovie_id)
            movieListPresenter = MovieDetailsPresenter(this)
            if(isNetworkAvailable(this)){
                movieListPresenter!!.requestMovieDetailsData(strMovie_id!!)
            }
        }
        initCollapsingToolbar();
        setcast()
    }

    private fun initCollapsingToolbar() {
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar.title = " "

        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        appBarLayout.setExpanded(true)

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = mMovieModal.title
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    override fun showProgress() {
       // pb_load_backdrop.visibility = View.VISIBLE
        pb_cast_loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
      //  pb_load_backdrop.visibility = View.GONE
        pb_cast_loading.visibility = View.GONE
    }

    override fun showEmptyView() {

    }

    override fun hideEmptyView() {

    }
    override fun setDtaToView(mModal: Movie) {
        Log.e("tag","MovieDetails" + Gson().toJson(mModal))
        setView(mModal)
    }

    private fun setView(mModal: Movie) {
        mMovieModal = mModal
        Glide.with(this)
            .load(BACKDROP_BASE_URL + mModal.getBackdropPath())
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    pb_load_backdrop.setVisibility(View.GONE)
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    pb_load_backdrop.setVisibility(View.GONE)
                    return false
                }

            })
            .apply(RequestOptions().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
            .into(iv_backdrop)
        tv_movie_title.text =mModal.getTitle()
        tv_release_date.text =mModal.releaseDate
        tv_movie_ratings.text = mModal.rating.toString()
        tv_overview_title.text = mModal.overview
        if(TextUtils.isEmpty(mModal.tagline)){
            tv_tagline_value.text = "N/A"
        }else{
            tv_tagline_value.text = mModal.tagline
        }
        if(TextUtils.isEmpty(mModal.homepage)){
            tv_homepage_value.text = "N/A"
        }else{
            tv_homepage_value.text = mModal.homepage
        }
        if(TextUtils.isEmpty(mModal.runTime)){
            tv_runtime_value.text = "N/A"
        }else{
            tv_runtime_value.text = mModal.runTime
        }

        mCastArray.clear();
        mCastArray.addAll(mModal.getCredits().getCast());
        castAdapter!!.notifyDataSetChanged();


    }

    private fun setcast() {
        castAdapter =  CastAdapter(this, mCastArray);
        rv_cast.setAdapter(castAdapter);

    }

}

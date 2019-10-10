package com.aru.androidmvpbasic.moviedetails

import android.util.Log
import com.aru.androidmvpbasic.Api.ApiConstant
import com.aru.androidmvpbasic.Api.ApiInitialize
import com.aru.androidmvpbasic.Api.ApiRequest
import com.aru.androidmvpbasic.Api.ApiResponseManager
import com.aru.androidmvpbasic.modal.Movie
import com.aru.androidmvpbasic.modal.MovieListResponse
import com.aru.androidmvpbasic.movielist.presenter.MovieListview
import com.aru.androidmvpbasic.util.AppConstant.CREDITS
import com.google.gson.Gson

class MovieDetailsPresenter(internal var movieDetailsActivity: MovieDetailsActivity):MovieListview.MovieDetailPresenter,
    MovieListview.ApiResponseInterface{

    private var mProgressbar: MovieListview.View? = null
    private var mShowEmptyView: MovieListview.ShowEmptyView? = null
    private var mMovieDataPresenter: MovieListview.MovieDetailsDataPresenter? = null

    override fun requestMovieDetailsData(movieId: String) {
        mProgressbar = movieDetailsActivity
        mProgressbar!!.showProgress()
        ApiRequest<Any>(movieDetailsActivity, ApiInitialize.initializes()
            .getMovieDetails(movieId!!,ApiConstant.API_KEY,CREDITS),
            2, true,this)
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        var mModal = apiResponseManager.response as Movie
        mMovieDataPresenter = movieDetailsActivity
        mShowEmptyView = movieDetailsActivity
        mProgressbar!!.hideProgress()
        if(apiResponseManager.type == 2){

            if(mModal == null){
                mShowEmptyView!!.showEmptyView()
            }else{
                mMovieDataPresenter!!.setDtaToView(mModal)
                mShowEmptyView!!.hideEmptyView()
            }

        }else if(apiResponseManager.type == 412){
            mShowEmptyView!!.hideEmptyView()
        }
    }
}

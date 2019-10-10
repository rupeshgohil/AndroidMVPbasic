package com.aru.androidmvpbasic.movielist.presenter

import com.aru.androidmvpbasic.Api.ApiConstant.API_KEY
import com.aru.androidmvpbasic.Api.ApiInitialize
import com.aru.androidmvpbasic.Api.ApiRequest
import com.aru.androidmvpbasic.Api.ApiResponseManager
import com.aru.androidmvpbasic.modal.MovieListResponse
import com.aru.androidmvpbasic.movielist.MainActivity

class MovieListPresenter(internal var mcontext: MainActivity) : MovieListview.Presenter,
    MovieListview.ApiResponseInterface{


    private var mProgressbar: MovieListview.View? = null
    private var mShowEmptyView: MovieListview.ShowEmptyView? = null
    private var mMovieDataPresenter: MovieListview.MovieDataPresenter? = null
    override fun requestMovieData(page: Int) {
        mProgressbar = mcontext
        mProgressbar!!.showProgress()
        ApiRequest<Any>(mcontext, ApiInitialize.initializes()
            .getMovieList(API_KEY,page),
            1, true,this)

    }
    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
            var mModal = apiResponseManager.response as MovieListResponse
        mMovieDataPresenter = mcontext
        mShowEmptyView = mcontext
        mProgressbar!!.hideProgress()
        if(apiResponseManager.type == 1){
            if(mModal.results.isEmpty()){
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

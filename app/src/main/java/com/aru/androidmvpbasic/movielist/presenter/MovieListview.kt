package com.aru.androidmvpbasic.movielist.presenter

import com.aru.androidmvpbasic.Api.ApiResponseManager
import com.aru.androidmvpbasic.modal.Movie
import com.aru.androidmvpbasic.modal.MovieListResponse

interface MovieListview {

    interface View {
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter {
        fun requestMovieData(movieId: Int)

    }
    interface MovieDetailPresenter {
        fun requestMovieDetailsData(movieId: String)

    }

    interface ResponceApi {
        fun onFail()
        fun  onSuccess(response: Any?)
    }

    interface ApiResponseInterface{
        fun getApiResponse(apiResponseManager: ApiResponseManager<*>)
    }
    interface ShowEmptyView {
        fun showEmptyView()
        fun hideEmptyView()
    }
    interface MovieDataPresenter{
        fun setDtaToView(mModal: MovieListResponse)

    }
    interface MovieDetailsDataPresenter{
        fun setDtaToView(mModal: Movie)

    }

}

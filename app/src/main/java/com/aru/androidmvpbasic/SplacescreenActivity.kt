package com.aru.androidmvpbasic

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.aru.androidmvpbasic.movielist.MainActivity


class SplacescreenActivity : BaseActivity() {
    internal var mHandler: Handler?= null
    internal var mRunnable: Runnable = Runnable { gotoScene() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splacescreen)
        onHandler()
    }
    private fun onHandler() {
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, SPLASH_TIME_OUT.toLong())
    }
    private fun gotoScene() {
       var mIntent = Intent(this, MainActivity::class.java)
        startActivity(mIntent)
        finish()
    }
    companion object {
        val SPLASH_TIME_OUT = 3000
    }
}

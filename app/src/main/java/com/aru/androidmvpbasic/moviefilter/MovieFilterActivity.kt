package com.aru.androidmvpbasic.moviefilter

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.aru.androidmvpbasic.BaseActivity
import com.aru.androidmvpbasic.R

import kotlinx.android.synthetic.main.activity_movie_filter.*
import java.util.*
import android.app.Activity
import com.aru.androidmvpbasic.util.AppConstant.KEY_RELEASE_TO
import com.aru.androidmvpbasic.util.AppConstant.KEY_RELEASE_FROM
import android.content.Intent
import android.util.Log

class MovieFilterActivity : BaseActivity() {
     var fromDate:String? = ""
     var toDate:String? = ""
    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var mYearEnd: Int = 0
    var mMonthEnd: Int = 0
    var mDayEnd: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_filter)
        tv_from_date.setOnClickListener {
            fromdate()
        }
        tv_to_date.setOnClickListener {
            todate()
        }
        btn_filter.setOnClickListener {
            if(fromDate!!.isEmpty()){
                val snackbar = Snackbar
                    .make(btn_filter, "Select start date", Snackbar.LENGTH_LONG)
                snackbar.show()
            }else if(toDate!!.isEmpty()){
                val snackbar = Snackbar
                    .make(btn_filter, "Select end date", Snackbar.LENGTH_LONG)
                snackbar.show()
            }else{
                val returnIntent = Intent()
                returnIntent.putExtra(KEY_RELEASE_FROM, fromDate)
                returnIntent.putExtra(KEY_RELEASE_TO, toDate)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }

    private fun todate() {
        var c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth  = c.get(Calendar.MONTH)
        mDay  = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = android.app.DatePickerDialog(this,
            android.app.DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                //var mdate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                var mdate = year.toString() + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                Log.e("Todate",mdate)
                fromDate = mdate
                tv_from_date.text = mdate
            }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun fromdate() {
        var c = Calendar.getInstance()
        var mYear = c.get(Calendar.YEAR)
       var mMonth  = c.get(Calendar.MONTH)
       var  mDay  = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = android.app.DatePickerDialog(this,
            android.app.DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                //var mdate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                var mdate = year.toString() + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                Log.e("Startdate",mdate)
                toDate = mdate
                tv_to_date.text = mdate
            }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

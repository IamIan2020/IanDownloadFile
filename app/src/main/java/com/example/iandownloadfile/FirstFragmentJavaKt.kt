package com.example.iandownloadfile

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.iandownloadfile.databinding.FragmentFirstBinding
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask

class FirstFragmentJavaKt : Fragment() {
    private val mHandler = Handler(Looper.getMainLooper())
    protected var spInfo: SpInfo? = null
    protected var log: String = javaClass.simpleName
    private var timer: Timer? = null
    private var task: TimerTask? = null
    private val pwTimer = "pw_timer"
    private var binding: FragmentFirstBinding? = null

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            LogUtils.e(log, "eeeeeeeee " + System.currentTimeMillis())
            mHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spInfo = SpInfo(requireContext())
        binding!!.buttonFirst.setOnClickListener { NavHostFragment.findNavController(this@FirstFragmentJavaKt).navigate(R.id.action_FirstFragment_to_SecondFragment) }
        val now = Calendar.getInstance()
        now.add(Calendar.DATE, 1)
        now.add(Calendar.HOUR_OF_DAY, 2)
        now.add(Calendar.MINUTE, 1)
        now.add(Calendar.SECOND, 1)
        val teenMinutesFromNow = now.time
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN)
        val nowDate = df.format(teenMinutesFromNow)
        spInfo!!.putSP(pwTimer, nowDate)
        if (timer != null) {
            timer!!.cancel()
        }
        setTimer()
        mHandler.postDelayed(runnable, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setTimer() {
        if (timer != null) timer!!.cancel()
        timer = Timer()
        task = object : TimerTask() {
            @SuppressLint("SetTextI18n", "DefaultLocale")
            override fun run() {
                val diff: Long
                val second: Long
                val day: Long
                val hour: Long
                val min: Long
                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN)
                val c1 = Calendar.getInstance(TimeZone.getDefault())
                var endDate1 = spInfo!!.getString(pwTimer) //, "2022-01-01 00:00:00"
                if (endDate1.isEmpty()) endDate1 = "2000-01-01 00:00:00"
                val c2 = Calendar.getInstance(TimeZone.getDefault())
                try {
                    c2.time = df.parse(endDate1)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val milis1 = c1.timeInMillis
                val milis2 = c2.timeInMillis
                diff = milis2 - milis1
                day = diff / (1000 * 60 * 60 * 24)
                hour = diff / (60 * 60 * 1000) - day * 24
                min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
                second = diff / 1000 % 60
                LogUtils.e(log, "day== $day hour== $hour min== $min second== $second")
                //                    layout.verifyBtn.setText("00:"+ String.format("%02d",second ));
//                    layout.verifyBtn.setClickable(false);
//                if (second <= 0) { //時間過了
//                    timer.cancel();
//                }
            }
        }
        timer!!.schedule(task, 0, 1000)
    }

    override fun onPause() {
        mHandler.removeCallbacks(runnable)
        super.onPause()
    }
}
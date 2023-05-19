package com.example.iandownloadfile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.iandownloadfile.databinding.FragmentFirstBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class FirstFragment extends Fragment {
    protected SpInfo spInfo;
    protected String log = getClass().getSimpleName();
    private Timer timer;
    private TimerTask task;
    private String pwTimer = "pw_timer";
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spInfo = new SpInfo(requireContext());
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 1);
        now.add(Calendar.HOUR_OF_DAY, 2);
        now.add(Calendar.MINUTE, 1);
        now.add(Calendar.SECOND, 1);
        Date teenMinutesFromNow = now.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN);
        String nowDate = df.format(teenMinutesFromNow);
        spInfo.putSP(pwTimer, nowDate);
        if (timer!= null){
            timer.cancel();
        }
        setTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setTimer() {
        if (timer != null) timer.cancel();
        timer = new Timer();
        task = new TimerTask() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void run() {
                long diff = 0;
                long second = 0;
                long day = 0L;
                long hour = 0L;
                long min = 0L;
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN);
                Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
                String endDate1 = spInfo.getString(pwTimer);//, "2022-01-01 00:00:00"
                if (endDate1.isEmpty()) endDate1 = "2000-01-01 00:00:00";
                Calendar c2 = Calendar.getInstance(TimeZone.getDefault());
                try {
                    c2.setTime(df.parse(endDate1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long milis1 = c1.getTimeInMillis();
                long milis2 = c2.getTimeInMillis();
                diff = milis2 - milis1;

                day = diff / (1000 * 60 * 60 * 24);
                hour = diff / (60 * 60 * 1000) - day * 24;
                min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
                second = (diff / 1000) % 60;
                LogUtils.e(log, "day== " + day + " hour== " + hour + " min== " + min + " second== " + second);
//                    layout.verifyBtn.setText("00:"+ String.format("%02d",second ));
//                    layout.verifyBtn.setClickable(false);
//                if (second <= 0) { //時間過了
//                    timer.cancel();
//                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

}
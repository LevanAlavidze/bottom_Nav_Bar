package com.example.quitsmoking.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quitsmoking.R;
import com.example.quitsmoking.TimeManager;

import java.util.Calendar;

public class Home_Fragment extends Fragment {
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    private SharedPreferences preferences;
    private long baseTime;
    private TextView tvDayCounter, tvWeekCounter, tvMonthCounter;
    int dailyCount;
    int weeklyCount;
    int monthlyCount;
    private long lastSavedTime;
    private TimeManager timeManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        chronometer = rootView.findViewById(R.id.chronometer);
        Button startButton = rootView.findViewById(R.id.btn_start);
        Button resetButton = rootView.findViewById(R.id.btn_reset);
        Button btn_counter = rootView.findViewById(R.id.btn_counter);
        tvDayCounter = rootView.findViewById(R.id.tvDayCounter);
        tvWeekCounter = rootView.findViewById(R.id.tvWeekCounter);
        tvMonthCounter = rootView.findViewById(R.id.tvMonthCounter);
        timeManager = new TimeManager(); // Initialize the TimeManager

        preferences = requireContext().getSharedPreferences("SP_Chronometer", MODE_PRIVATE);

        lastSavedTime = preferences.getLong("lastSavedTime", 0);
        dailyCount = preferences.getInt("dailyCount", 0);
        weeklyCount = preferences.getInt("weeklyCount", 0);
        monthlyCount = preferences.getInt("monthlyCount", 0);


        running = preferences.getBoolean("running", false);
        pauseOffset = preferences.getLong("pause_offset", 0);
        baseTime = preferences.getLong("base_time", SystemClock.elapsedRealtime());

        if (running) {
            chronometer.setBase(baseTime);
            chronometer.start();
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    baseTime = SystemClock.elapsedRealtime() - pauseOffset;
                    chronometer.setBase(baseTime);
                    chronometer.start();
                    running = true;
                }
            }
        });

        btn_counter.setOnClickListener(v -> {
            // Increment the daily count
            dailyCount++;
            // Increment the weekly count
            weeklyCount++;
            // Increment the monthly count
            monthlyCount++;

            // Update the TextViews with the new counts
            updateTextViews();

            lastSavedTime = System.currentTimeMillis();

            saveCountsToSharedPreferences();

        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                baseTime = SystemClock.elapsedRealtime();
                pauseOffset = 0;
                running = false;
            }
        });

        return rootView;
    }

    void checkAndResetCounters() {
        Calendar calendar = Calendar.getInstance();
        long currentTime = System.currentTimeMillis();

        if (currentTime >= getLastResetTime(Calendar.DAY_OF_MONTH)) {
            // Reset daily counter
            dailyCount = 0;
        }

        if (currentTime >= getLastResetTime(Calendar.WEEK_OF_YEAR)) {
            // Reset weekly counter
            weeklyCount = 0;
        }

        if (currentTime >= getLastResetTime(Calendar.MONTH)) {
            // Reset monthly counter
            monthlyCount = 0;
        }

        // Update the last saved time if any counter was reset
        if (dailyCount == 0 || weeklyCount == 0 || monthlyCount == 0) {
            lastSavedTime = currentTime;
        }
    }

    private long getLastResetTime(int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastSavedTime);
        switch (interval) {
            case Calendar.DAY_OF_MONTH:
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case Calendar.WEEK_OF_YEAR:
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case Calendar.MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.add(Calendar.MONTH, 1);
                break;
        }
        return calendar.getTimeInMillis();
    }

    private void updateTextViews() {
        tvDayCounter.setText(String.valueOf(dailyCount));
        tvWeekCounter.setText(String.valueOf(weeklyCount));
        tvMonthCounter.setText(String.valueOf(monthlyCount));
    }

    private void saveCountsToSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("running", running);
        editor.putLong("pause_offset", pauseOffset);
        editor.putLong("base_time", baseTime);
        editor.putInt("dailyCount",dailyCount);
        editor.putInt("weeklyCount",weeklyCount);
        editor.putInt("monthlyCount",monthlyCount);
        editor.putLong("lastSavedTime", lastSavedTime);
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveCountsToSharedPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        running = preferences.getBoolean("running", false);
        pauseOffset = preferences.getLong("pause_offset", 0);
        baseTime = preferences.getLong("base_time", SystemClock.elapsedRealtime());
        checkAndResetCounters();
        updateTextViews();
        if (running) {
            chronometer.setBase(baseTime);
            chronometer.start();
        }
    }
}

package com.example.quitsmoking;

import java.util.Calendar;

public class TimeManager { public long getCurrentTimeInMillis() {
    return System.currentTimeMillis();
}

    public long getLastResetTimeInMillis(long lastSavedTime, int interval) {
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
}
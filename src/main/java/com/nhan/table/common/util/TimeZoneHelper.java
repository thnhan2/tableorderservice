package com.nhan.table.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneHelper {
    public static Date convertToTimeZone(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        return calendar.getTime();
    }


}

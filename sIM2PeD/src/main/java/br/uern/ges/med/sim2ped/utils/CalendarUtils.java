package br.uern.ges.med.sim2ped.utils;


import java.util.Calendar;

/**
 * Created by Rodrigo on 28/08/2014 (13:28).
 *
 *
 * Package:
 */
public class CalendarUtils {

    public static Calendar convertDateNextWeekday(long timeStamp){

        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.setTimeInMillis(timeStamp);

        //Log.w(Constant.TAG_LOG, "pastCalendar = " + pastCalendar.getTime().toString());

        Calendar calendarReturn = Calendar.getInstance();

        //Log.w(Constant.TAG_LOG, "calendarReturn = " + calendarReturn.getTime().toString());

        calendarReturn.set(Calendar.DAY_OF_WEEK, pastCalendar.get(Calendar.DAY_OF_WEEK));
        calendarReturn.set(Calendar.HOUR_OF_DAY, pastCalendar.get(Calendar.HOUR_OF_DAY));
        calendarReturn.set(Calendar.MINUTE, pastCalendar.get(Calendar.MINUTE));
        calendarReturn.set(Calendar.SECOND, pastCalendar.get(Calendar.SECOND));

        //Log.w(Constant.TAG_LOG, "calendarReturn = " + calendarReturn.getTime().toString());

        if(calendarReturn.before(Calendar.getInstance()))
            calendarReturn.add(Calendar.WEEK_OF_YEAR, 1);

        //Log.w(Constant.TAG_LOG, "calendarReturn = " + calendarReturn.getTime().toString());

        return calendarReturn;
    }

    public static Calendar addWeekInDate(long timeStamp){

        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.setTimeInMillis(timeStamp);

        Calendar calendarReturn = Calendar.getInstance();

        calendarReturn.set(Calendar.DAY_OF_WEEK, pastCalendar.get(Calendar.DAY_OF_WEEK));
        calendarReturn.set(Calendar.HOUR_OF_DAY, pastCalendar.get(Calendar.HOUR_OF_DAY));
        calendarReturn.set(Calendar.MINUTE, pastCalendar.get(Calendar.MINUTE));
        calendarReturn.set(Calendar.SECOND, pastCalendar.get(Calendar.SECOND));

        calendarReturn.add(Calendar.WEEK_OF_YEAR, 1);

        return calendarReturn;
    }

    public static Calendar getHours24HoursAfter(){

        Calendar cal = Calendar.getInstance();

        long last24h = 1000 * 60 * 60 * 24;
        long time = (cal.getTimeInMillis() - last24h);

        cal.setTimeInMillis(time);

        return cal;
    }
}

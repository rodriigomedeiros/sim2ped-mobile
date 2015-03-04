package br.uern.ges.med.sim2ped.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.uern.ges.med.sim2ped.beans.Routine;
import br.uern.ges.med.sim2ped.model.RoutineModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.CalendarUtils;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 03/09/2014 (10:51).
 *
 */
public class Alarm {

    public static void setAlarmCycleStart(Context context){
        RoutineModel routineModel = new RoutineModel(context);

        Preferences preferences = new Preferences(context);
        ArrayList<Routine> lostRoutines = routineModel.getLostRoutines(preferences.getUserCodeLogged());

        for (Routine routine : lostRoutines) {
            Calendar newDate = CalendarUtils.convertDateNextWeekday(routine.getTime());

            routine.setTime(newDate.getTimeInMillis());

            routineModel.updateRoutine(routine);
        }

        Routine routine;

        routine = routineModel.getNextRoutine(preferences.getUserCodeLogged());

        Log.i(Constant.TAG_LOG, "StartCycle Alarm: "+routine.getTime());

        if(routine.getId() != 0) {
            setAlarm(context, routine);

            routine.setTime(CalendarUtils.addWeekInDate(routine.getTime()).getTimeInMillis());
            routineModel.updateRoutine(routine);
        }

    }

    public static void setAlarm(Context context, Routine routine) {
        AlarmManager alaManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Preferences pref = new Preferences(context);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(routine.getTime());

        // Add early minutes
        cal.add(Calendar.MINUTE, (Integer.parseInt(pref.getPrefEarlyMinutesForAlarm())*(-1)));

        alaManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), getIntentPendent(context, routine.getId()));

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(routine.getTime());

        Log.v(Constant.TAG_LOG, "Alarm configured: "+calendar.toString());

        Log.i(Constant.TAG_LOG, "StartupAlarm - Alarm configured");
    }

    public static void cancelAlarm(Context context) {
        /*
        try {
            AlarmManager alaManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alaManager.cancel(getIntentPendent(context, routine.getId()));
            Log.i(Constant.TAG_LOG, "StartupAlarm - Canceling alarms");
        } catch (Exception e) {
            Log.e(Constant.TAG_LOG, "StartupAlarm - Update was not canceled. " + e.toString());
        } */

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent updateServiceIntent = new Intent("br.uern.ges.med.sim2ped.TIME_TO_CARE");
        PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);

        // Cancel alarms
        try {
            alarmManager.cancel(pendingUpdateIntent);
        } catch (Exception e) {
            Log.e(Constant.TAG_LOG, "StartpAlarm - update was not canceled. " + e.toString());

        }
    }

    public static PendingIntent getIntentPendent(Context context, long routineId) {
        Intent intent = new Intent("br.uern.ges.med.sim2ped.TIME_TO_CARE");
        intent.putExtra(Constant.BUNDLE_ROUTINE_ID, routineId);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

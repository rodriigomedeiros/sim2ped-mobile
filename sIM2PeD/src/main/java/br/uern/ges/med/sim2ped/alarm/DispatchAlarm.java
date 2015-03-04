package br.uern.ges.med.sim2ped.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.beans.Care;
import br.uern.ges.med.sim2ped.beans.Routine;
import br.uern.ges.med.sim2ped.managers.Notifications;
import br.uern.ges.med.sim2ped.model.CaresModel;
import br.uern.ges.med.sim2ped.model.ContextModel;
import br.uern.ges.med.sim2ped.model.RoutineModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.CalendarUtils;
import br.uern.ges.med.sim2ped.utils.Constant;
 
public class DispatchAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(Constant.TAG_LOG, "Time to care - DispatchAlarm");

        Bundle bundle = intent.getExtras();
        long idRoutine = bundle.getLong(Constant.BUNDLE_ROUTINE_ID);

        RoutineModel routineModel = new RoutineModel(context);
        ContextModel contextModel = new ContextModel(context);
        CaresModel caresModel = new CaresModel(context);
        Notifications notification = new Notifications(context);

        Preferences preferences = new Preferences(context);
        Routine nextRoutine = routineModel.getNextRoutine(preferences.getUserCodeLogged());

        Alarm.setAlarm(context, nextRoutine);

        nextRoutine.setTime(CalendarUtils.addWeekInDate(nextRoutine.getTime()).getTimeInMillis());

        routineModel.updateRoutine(nextRoutine);

        Routine actualRoutine = routineModel.getRoutine(idRoutine);

        long careId = contextModel.getRandomCareIdInContext(actualRoutine.getContextId());
        Care care = caresModel.get(careId);

        String[] keys = new String[2], values = new String[2];
        keys[0] = Constant.BUNDLE_CARE_ID;
        keys[1] = Constant.BUNDLE_ROUTINE_ID;
        values[0] = String.valueOf(careId);
        values[1] = String.valueOf(actualRoutine.getId());

        notification.addNotification(
                Constant.NOTIF_ID_NEW_CARE,
                context.getString(R.string.notif_new_care_title),
                care.getCareQuestion(),
                keys,
                values,
                context.getString(R.string.notif_new_care_ticker),
                true);
    }
}

package br.uern.ges.med.sim2ped.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.alarm.Alarm;
import br.uern.ges.med.sim2ped.beans.Care;
import br.uern.ges.med.sim2ped.beans.CaresInContext;
import br.uern.ges.med.sim2ped.beans.Context;
import br.uern.ges.med.sim2ped.beans.Routine;
import br.uern.ges.med.sim2ped.beans.Tip;
import br.uern.ges.med.sim2ped.managers.Notifications;
import br.uern.ges.med.sim2ped.model.CaresModel;
import br.uern.ges.med.sim2ped.model.ContextModel;
import br.uern.ges.med.sim2ped.model.RoutineModel;
import br.uern.ges.med.sim2ped.model.TipsModel;
import br.uern.ges.med.sim2ped.utils.Constant;
import br.uern.ges.med.sim2ped.utils.JSONMessageObjectGCM;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;


public class GcmIntentService extends IntentService {

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				JSONMessageObjectGCM gcmMessage = new Gson().fromJson(extras.getString("message"),
                        JSONMessageObjectGCM.class);

				if(gcmMessage.getType().equals(Constant.JSON_OBJECT_TAG_TIP)){ // Arriving message of type TIP

                    Log.i(Constant.TAG_LOG, "Tip: " + gcmMessage.getObject());
                    Tip tip = new Gson().fromJson(gcmMessage.getObject(), Tip.class);
                    TipsModel tipM = new TipsModel(getApplicationContext());

                    if(gcmMessage.getAction().equals(Constant.JSON_ACTION_INSERT)) {
                        tipM.insert(tip);
                        showNotificationNewTip(tip);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_UPDATE)) {
                        tipM.update(tip);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_DELETE)){
                        tipM.delete(tip.getID());
                    }

				} else if(gcmMessage.getType().equals(Constant.JSON_OBJECT_TAG_CARE)){

                    Log.i(Constant.TAG_LOG, "New Care: " + gcmMessage.getObject());
                    Care care = new Gson().fromJson(gcmMessage.getObject(), Care.class);
					CaresModel careM = new CaresModel(getApplicationContext());
                    ContextModel contextModel = new ContextModel(getApplicationContext());


                    if(gcmMessage.getAction().equals(Constant.JSON_ACTION_INSERT)) {
                        careM.insert(care);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_UPDATE)) {
                        careM.update(care);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_DELETE)){
                        careM.delete(care.getID());
                        contextModel.deleteCaresInContext_CARE(care.getID());
                    }

				} else if(gcmMessage.getType().equals(Constant.JSON_OBJECT_TAG_CONTEXT)){

                    Log.i(Constant.TAG_LOG, "New Context: " + gcmMessage.getObject());
                    Context context = new Gson().fromJson(gcmMessage.getObject(), Context.class);
                    ContextModel contextModel = new ContextModel(getApplicationContext());

                    if(gcmMessage.getAction().equals(Constant.JSON_ACTION_INSERT)) {
                        contextModel.insert(context);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_UPDATE)) {
                        contextModel.update(context);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_DELETE)){
                        contextModel.delete(context.getId());
                        contextModel.deleteCaresInContext_CONTEXT(context.getId());
                    }

                } else if(gcmMessage.getType().equals(Constant.JSON_OBJECT_TAG_CARES_IN_CONTEXT)){

                    Log.i(Constant.TAG_LOG, "New Cares in Context: " + gcmMessage.getObject());
                    CaresInContext caresInContext = new Gson().fromJson(gcmMessage.getObject(),
                            CaresInContext.class);
                    ContextModel contextModel = new ContextModel(getApplicationContext());

                    if(gcmMessage.getAction().equals(Constant.JSON_ACTION_INSERT)) {
                        contextModel.inserCaresInContext(caresInContext);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_DELETE)){
                        if(caresInContext.getCareId() > 0){
                            contextModel.deleteCaresInContext_CARE(caresInContext.getCareId());
                        } else if(caresInContext.getContextId() > 0){
                            contextModel.deleteCaresInContext_CONTEXT(caresInContext.getContextId());
                        }
                    }

                } else if(gcmMessage.getType().equals(Constant.JSON_OBJECT_TAG_ROUTINE)){

                    Log.i(Constant.TAG_LOG, "New Routine: " + gcmMessage.getObject());
                    Routine routine = new Gson().fromJson(gcmMessage.getObject(),
                            Routine.class);
                    RoutineModel routineModel = new RoutineModel(getApplicationContext());

                    if(gcmMessage.getAction().equals(Constant.JSON_ACTION_INSERT)) {
                        routineModel.insert(routine);

                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_UPDATE)) {
                        routineModel.updateRoutine(routine);
                    } else if(gcmMessage.getAction().equals(Constant.JSON_ACTION_DELETE)){
                        routineModel.delete(routine.getId());
                    }

                    Alarm.cancelAlarm(getApplicationContext());
                    Alarm.setAlarmCycleStart(getApplicationContext());

                }

            }
		}
		
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	
	private void showNotificationNewTip(Tip tip) {
		
		
		Notifications notification = new Notifications(this);

        String[] keys = new String[1], values = new String[1];
        keys[0] = Constant.BUNDLE_TIP_ID;
        values[0] = String.valueOf(tip.getID());

		notification.addNotification(Constant.NOTIF_ID_NEW_TIP, this.getString(R.string.notif_new_tip_title), this.getString(R.string.notif_new_tip_content), keys, values, false);
	}
}

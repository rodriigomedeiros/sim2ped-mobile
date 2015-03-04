package br.uern.ges.med.sim2ped.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.uern.ges.med.sim2ped.utils.Constant;

public class StartupAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			Log.i(Constant.TAG_LOG, "StartupAlarm - Boot Completed in Device, I go define Alarm!");
			Alarm.setAlarmCycleStart(context);
		}

        Log.i(Constant.TAG_LOG, "StartupAlarm - onReceive();");

	}

}

package com.alexpertsev.scambuster;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ScamBuster extends BroadcastReceiver {

	List<String> unwantedNumbers = Arrays.asList("+18558255331", "+18664550729", "+18555613603","+18558265904","+18007692516, +118555613603");
	
	@Override
	public void onReceive(Context context, Intent intent) {

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		   try {
		     Class<?> c = Class.forName(tm.getClass().getName());
		     Method m = c.getDeclaredMethod("getITelephony");
		     m.setAccessible(true);
		     //com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);

             Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
		     Bundle bundle = intent.getExtras();

		     String phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		     //Log.d("INCOMING", phoneNumber);
		     if ((unwantedNumbers.contains(phoneNumber))) {

		    	 Log.d("Got a phone call from :", phoneNumber);
                 Class<?> telephonyServiceClass = Class.forName(telephonyService.getClass().getName());

                 //Method silenceRinger = telephonyServiceClass.getDeclaredMethod("silenceRinger");
                 //silenceRinger.setAccessible(true);
                 Method endCall = telephonyServiceClass.getDeclaredMethod("endCall");
                 endCall.setAccessible(true);
                 //silenceRinger.invoke(telephonyService);
                 endCall.invoke(telephonyService);
		        Log.d("HANG UP", phoneNumber);
		     }

		   } catch (Exception e) {
		     e.printStackTrace();
		   }
	}
}

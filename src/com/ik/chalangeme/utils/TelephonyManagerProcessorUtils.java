package com.ik.chalangeme.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import com.ik.chalangeme.constants.ActiveRecord;

public class TelephonyManagerProcessorUtils {
     private TelephonyManager telManager;

     public TelephonyManagerProcessorUtils () {
     }

     /**
      * Parse TelephonyManager values into a human readable string wrapped to
      * DEVICEINFO object.
      * 
      * @param telMgr
      * @return
      */
     public DeviceInfo getTelephonyOverview(Activity activity) {

          if ( null == telManager ) {
               telManager = (TelephonyManager) ActiveRecord.context.getSystemService(Context.TELEPHONY_SERVICE);
          }

          // telManager.get

          // CALL STATE
          int callState = telManager.getCallState();
          String callStateString = "NA";
          switch (callState) {
               case TelephonyManager.CALL_STATE_IDLE:
                    callStateString = "IDLE";
                    break;
               case TelephonyManager.CALL_STATE_OFFHOOK:
                    callStateString = "OFFHOOK";
                    break;
               case TelephonyManager.CALL_STATE_RINGING:
                    callStateString = "RINGING";
                    break;
          }

          // GSM CELL INFORMATION
          String cellLocationString = "UNKNOWN";
          try {
               GsmCellLocation cellLocation = (GsmCellLocation) telManager.getCellLocation();
               cellLocationString = "GSM Location Area Code" + cellLocation.getLac() + ", GSM Cell Id " + cellLocation.getCid();
          } catch (Exception ex) {
          }

          // DEVICE INFO
          String deviceId = getDeviceID(activity, telManager);// telManager.getDeviceId();
          String deviceSoftwareVersion = telManager.getDeviceSoftwareVersion();
          String line1Number = telManager.getLine1Number();
          String networkCountryIso = telManager.getNetworkCountryIso();
          String networkOperator = telManager.getNetworkOperator();
          String networkOperatorName = telManager.getNetworkOperatorName();
          String simCountryIso = telManager.getSimCountryIso();
          String simOperator = telManager.getSimOperator();
          String simOperatorName = telManager.getSimOperatorName();
          String simSerialNumber = telManager.getSimSerialNumber();
          String simSubscriberId = telManager.getSubscriberId();
          String networkType = "";

          switch (telManager.getNetworkType()) {

               case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    networkType = String.valueOf("NETWORK_TYPE_UNKNOWN");
                    break;
               case TelephonyManager.NETWORK_TYPE_GPRS:
                    networkType = String.valueOf("NETWORK_TYPE_GPRS");
                    break;
               case TelephonyManager.NETWORK_TYPE_EDGE:
                    networkType = String.valueOf("NETWORK_TYPE_EDGE");
                    break;
               case TelephonyManager.NETWORK_TYPE_UMTS:
                    networkType = String.valueOf("NETWORK_TYPE_UMTS");
                    break;
               case TelephonyManager.NETWORK_TYPE_HSDPA:
                    networkType = String.valueOf("NETWORK_TYPE_HSDPA");
                    break;
               case TelephonyManager.NETWORK_TYPE_HSUPA:
                    networkType = String.valueOf("NETWORK_TYPE_HSUPA");
                    break;
               case TelephonyManager.NETWORK_TYPE_HSPA:
                    networkType = String.valueOf("NETWORK_TYPE_HSPA");
                    break;
               case TelephonyManager.NETWORK_TYPE_CDMA:
                    networkType = String.valueOf("NETWORK_TYPE_CDMA");
                    break;
               case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    networkType = String.valueOf("NETWORK_TYPE_EVDO_0");
                    break;
               case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    networkType = String.valueOf("NETWORK_TYPE_EVDO_A");
                    break;
               case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    networkType = String.valueOf("NETWORK_TYPE_EVDO_B");
                    break;
               case TelephonyManager.NETWORK_TYPE_1xRTT:
                    networkType = String.valueOf("NETWORK_TYPE_1xRTT");
                    break;
               case TelephonyManager.NETWORK_TYPE_IDEN:
                    networkType = String.valueOf("NETWORK_TYPE_IDEN");
                    break;
          }

          // PHONE TYPE
          int phoneType = telManager.getPhoneType();
          String phoneTypeString = "NA";
          switch (phoneType) {
               case TelephonyManager.PHONE_TYPE_GSM:
                    phoneTypeString = "GSM";
                    break;
               case TelephonyManager.PHONE_TYPE_NONE:
                    phoneTypeString = "NONE";
                    break;
          }

          // SIM CARD STATE
          int simState = telManager.getSimState();
          String simStateString = "NA";
          switch (simState) {
               case TelephonyManager.SIM_STATE_ABSENT:
                    simStateString = "ABSENT";
                    break;
               case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    simStateString = "NETWORK_LOCKED";
                    break;
               case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    simStateString = "PIN_REQUIRED";
                    break;
               case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    simStateString = "PUK_REQUIRED";
                    break;
               case TelephonyManager.SIM_STATE_READY:
                    simStateString = "STATE_READY";
                    break;
               case TelephonyManager.SIM_STATE_UNKNOWN:
                    simStateString = "STATE_UNKNOWN";
                    break;
          }

          // FILL DEVICEINFO OBJECT
          DeviceInfo deviceInfo = new DeviceInfo();
          deviceInfo.networkRoaming = String.valueOf(telManager.isNetworkRoaming()).equals("false") ? "Off" : "On";
          deviceInfo.networkType = networkType;
          deviceInfo.callState = callStateString;
          deviceInfo.cellLocationString = cellLocationString;
          deviceInfo.deviceId = deviceId;
          deviceInfo.deviceSoftwareVersion = deviceSoftwareVersion;
          deviceInfo.phoneNumber = line1Number;
          deviceInfo.networkCountryIso = networkCountryIso;
          deviceInfo.networkOperator = networkOperator;
          deviceInfo.networkOperatorName = networkOperatorName;
          deviceInfo.phoneTypeString = phoneTypeString;
          deviceInfo.simCountryIso = simCountryIso;
          deviceInfo.simOperator = simOperator;
          deviceInfo.simOperatorName = simOperatorName;
          deviceInfo.simSerialNumber = simSerialNumber;
          deviceInfo.simSubscriberId = simSubscriberId;
          deviceInfo.simStateString = simStateString;

          return deviceInfo;
     }

     /**
      * Return device id in stirng format
      * 
      * @param activity
      * @param manager
      * @return
      */
     private String getDeviceID(Activity activity, TelephonyManager manager) {
          String identifier = null;
          if ( manager != null ) {
               identifier = manager.getDeviceId();
          }

          if ( isStrContainsOnlyZeros(identifier) ) {
               identifier = Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID/* Secure.ANDROID_ID */);
          }
          return identifier;
     }

     /**
      * Check if all elements of string == 0
      * 
      * @param strToCheck
      * @return
      */
     private boolean isStrContainsOnlyZeros(String strToCheck) {
          if ( TextUtils.isEmpty(strToCheck) ) { return true; }
          for ( int i = 0; i < strToCheck.length(); i++ ) {
               // get i-th eleemnt of string
               String oneLetter = Character.toString(strToCheck.charAt(i));
               // check if it's equal 0
               if ( oneLetter.equals("0") ) {
                    continue;
               } else {
                    return false;
               }
          }
          return true;
     }
}

package com.maragues.menu_planner.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.maragues.menu_planner.App;


public class Connectivity {

  public boolean isNetworkAvailable() {
    if (App.appComponent.context() == null)
      return false;

    ConnectivityManager connectivityManager = (ConnectivityManager) App.appComponent.context().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
  }

  /**
   * Check if there is fast connectivity
   *
   * @param context
   * @return
   */
  public boolean isConnectedFast(Context context) {
    if (context == null)
      return false;

    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = cm.getActiveNetworkInfo();
    return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
  }

  /**
   * Check if the connection is fast
   *
   * @param type
   * @param subType
   * @return
   */
  public boolean isConnectionFast(int type, int subType) {
    if (type == ConnectivityManager.TYPE_WIFI) {
      return true;
    } else if (type == ConnectivityManager.TYPE_MOBILE) {
      switch (subType) {
        case TelephonyManager.NETWORK_TYPE_GPRS:
        case TelephonyManager.NETWORK_TYPE_1xRTT:
        case TelephonyManager.NETWORK_TYPE_CDMA:
        case TelephonyManager.NETWORK_TYPE_EDGE:
        case TelephonyManager.NETWORK_TYPE_IDEN:
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
          return false;
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
        case TelephonyManager.NETWORK_TYPE_HSDPA:
        case TelephonyManager.NETWORK_TYPE_HSPA:
        case TelephonyManager.NETWORK_TYPE_HSUPA:
        case TelephonyManager.NETWORK_TYPE_UMTS:
        case TelephonyManager.NETWORK_TYPE_EHRPD:
        case TelephonyManager.NETWORK_TYPE_EVDO_B:
        case TelephonyManager.NETWORK_TYPE_HSPAP:
        case TelephonyManager.NETWORK_TYPE_LTE:
          return true;
      }
    }

    return false;
  }
}

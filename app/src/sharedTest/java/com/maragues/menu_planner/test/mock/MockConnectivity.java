package com.maragues.menu_planner.test.mock;

import android.content.Context;

import com.maragues.menu_planner.utils.Connectivity;


/**
 * Created by maragues on 09/01/15.
 */
final public class MockConnectivity extends Connectivity {
  private static Boolean isConnectionFast = null;

  private static boolean isConnectivityEnabled = true;

  public static synchronized void setConnectivity(boolean isEnabled) {
    isConnectivityEnabled = isEnabled;
  }

  public static synchronized void setIsConnectionFast(Boolean isFast) {
    isConnectionFast = isFast;
  }

  @Override
  public boolean isNetworkAvailable() {
    return isConnectivityEnabled;
  }

  @Override
  public boolean isConnectedFast(Context context) {
    if (isConnectionFast == null)
      return super.isConnectedFast(context);

    return isConnectionFast;
  }
}

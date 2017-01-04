package com.maragues.menu_planner.test.mock;

import android.support.annotation.Nullable;

import com.maragues.menu_planner.utils.LocalTextUtils;

/**
 * Created by miguelaragues on 4/1/17.
 */
public class TestTextUtils extends LocalTextUtils {

  /**
   * Returns true if the string is null or 0-length.
   *
   * @param str the string to be examined
   * @return true if str is null or zero length
   */
  @Override
  public boolean isEmpty(@Nullable CharSequence str) {
    if (str == null || str.toString().trim().length() == 0)
      return true;
    else
      return false;
  }
}

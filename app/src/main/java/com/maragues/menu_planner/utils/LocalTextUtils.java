package com.maragues.menu_planner.utils;

import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;

/**
 * Created by maragues
 *
 * Duplicated methods to avoid android dependencies in presenters
 */
public class LocalTextUtils {
  /**
   * Returns true if the string is null or 0-length.
   * @param str the string to be examined
   * @return true if str is null or zero length
   */
  public boolean isEmpty(@Nullable CharSequence str) {
    return TextUtils.isEmpty(str);
  }

  public String capitalizeFirstLetter(String word){
    return stripToFirstLetterCapitalized(word) + word.substring(1);
  }

  public String stripToFirstLetterCapitalized(String word){
    return String.valueOf(Character.toUpperCase(word.charAt(0)));
  }

  @SuppressWarnings("deprecation")
  public CharSequence fromHtml(String text){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
    } else {
      return Html.fromHtml(text);
    }
  }
}

package com.maragues.menu_planner.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.maragues.menu_planner.R;

/**
 * Implementation of App Widget functionality.
 *
 * See https://github.com/manishcm/weatherwidget/blob/master/src/com/example/android/weatherlistwidget/WeatherWidgetProvider.java
 */
public class PlannerWidget extends AppWidgetProvider {

  public static final String EXTRA_MEAL_INSTANCE_ID = "extra_meal_instance_id";

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                              int appWidgetId) {
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.planner_widget);

    final Intent intent = new Intent(context, MealInstancesRemoteService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
    views.setRemoteAdapter(R.id.widget_listview, intent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}


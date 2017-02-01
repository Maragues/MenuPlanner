package com.maragues.menu_planner.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.meal_instance.MealInstanceViewerActivity;

import io.reactivex.disposables.Disposable;

/**
 * Implementation of App Widget functionality.
 * <p>
 * See https://github.com/manishcm/weatherwidget/blob/master/src/com/example/android/weatherlistwidget/WeatherWidgetProvider.java
 */
public class PlannerWidgetProvider extends AppWidgetProvider {

  public static final String EXTRA_MEAL_INSTANCE_ID = "extra_meal_instance_id";
  private static final String ACTION_CLICK_MEAL = "click_item_action";

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                              int appWidgetId) {
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.planner_widget);

    final Intent intent = new Intent(context, MealInstancesRemoteService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
    views.setRemoteAdapter(R.id.widget_listview, intent);

    //Enable item clicking
    final Intent onClickIntent = new Intent(context, PlannerWidgetProvider.class);
    onClickIntent.setAction(PlannerWidgetProvider.ACTION_CLICK_MEAL);
    onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
    final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0,
            onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    views.setPendingIntentTemplate(R.id.widget_listview, onClickPendingIntent);

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

  private Disposable disposable;

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
    disposable = MealInstancesRemoteService.MealInstancesRemoteViewsFactory.mealInstanceFlowable()
            .doOnNext(ignored -> onDataUpdated())
            .subscribe();
  }

  private void onDataUpdated() {
    AppWidgetManager widgetManager = AppWidgetManager.getInstance(App.appComponent.context());
    ComponentName cn = new ComponentName(App.appComponent.context(), PlannerWidgetProvider.class);
    widgetManager.notifyAppWidgetViewDataChanged(
            widgetManager.getAppWidgetIds(cn),
            R.id.widget_listview
    );
  }

  @Override
  public void onDisabled(Context context) {
    if (disposable != null && !disposable.isDisposed())
      disposable.dispose();
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    String action = intent.getAction();

    if (action == null)
      return;

    if (action.equals(ACTION_CLICK_MEAL)) {
      String mealId = intent.getStringExtra(EXTRA_MEAL_INSTANCE_ID);

      if (!App.appComponent.textUtils().isEmpty(mealId)) {
        Intent i = MealInstanceViewerActivity.createIntent(context, mealId);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        context.startActivity(i);
      }
    }
  }
}


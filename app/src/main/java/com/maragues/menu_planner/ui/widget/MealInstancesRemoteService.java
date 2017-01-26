package com.maragues.menu_planner.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.RecipeMeal;
import com.maragues.menu_planner.utils.DateUtils;
import com.maragues.menu_planner.utils.LocalTextUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Iterator;
import java.util.List;

/**
 * Created by miguelaragues on 27/1/17.
 */

public class MealInstancesRemoteService extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new MealInstancesRemoteViewsFactory(getApplicationContext(), intent);
  }

  static class MealInstancesRemoteViewsFactory implements RemoteViewsFactory {

    private final Context appContext;
    private final int widgetId;

    public MealInstancesRemoteViewsFactory(Context applicationContext, Intent intent) {
      this.appContext = applicationContext;
//      weakContext = new WeakReference<>(applicationContext);
      widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
              AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    List<MealInstance> meals;

    private List<MealInstance> getMealInstances() {
      if (meals == null) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = DateUtils.endOfDay(start.plusDays(6));

        meals = App.appComponent.mealInstanceProvider().listBetween(start, end)
                .blockingFirst();
      }

      return meals;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
      meals = null;

      getMealInstances();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
      return getMealInstances().size();
    }

    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("eeee");

    @Override
    public RemoteViews getViewAt(int position) {
      MealInstance mealInstance = getMealInstances().get(position);

      RemoteViews remoteViews = new RemoteViews(appContext.getPackageName(),
              R.layout.item_widget_mealinstance);

      String dateString = LocalTextUtils.capitalizeFirstLetter(DAY_FORMATTER.format(mealInstance.dateTime()))
              + " "
              + LocalTextUtils.capitalizeFirstLetter(mealInstance.labelId());

      remoteViews.setTextViewText(R.id.widget_item_label, dateString);

      String recipeString = "";
      Iterator<RecipeMeal> it = mealInstance.recipeCollection().iterator();
      while (it.hasNext()) {
        recipeString += LocalTextUtils.capitalizeFirstLetter(it.next().name());
        if (it.hasNext()) recipeString += "\n";
      }

      remoteViews.setTextViewText(R.id.widget_item_recipes, recipeString);

      final Intent fillInIntent = new Intent();
      final Bundle extras = new Bundle();
      extras.putString(PlannerWidget.EXTRA_MEAL_INSTANCE_ID, mealInstance.id());
      fillInIntent.putExtras(extras);
      remoteViews.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

      return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
      return null;
    }

    @Override
    public int getViewTypeCount() {
      return 1;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }
  }
}

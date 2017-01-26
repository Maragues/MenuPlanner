package com.maragues.menu_planner.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguelaragues on 27/1/17.
 */

public class WidgetAdapter extends ArrayAdapter<MealInstance> {
  private LayoutInflater inflater;

  public WidgetAdapter(@NonNull Context context,
                       @NonNull List<MealInstance> objects) {
    super(context, 0, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    WidgetMealInstanceHolder holder;
    if (convertView != null) {
      holder = (WidgetMealInstanceHolder) convertView.getTag();
    } else {
      convertView = getInflater().inflate(R.layout.item_widget_mealinstance, parent, false);
      holder = new WidgetMealInstanceHolder(convertView);
      convertView.setTag(holder);
    }

    holder.render(getItem(position));

    return convertView;
  }

  private LayoutInflater getInflater() {
    if (inflater == null)
      inflater = LayoutInflater.from(getContext());

    return inflater;
  }

  static class WidgetMealInstanceHolder {
    @BindView(R.id.widget_item_label)
    TextView textView;

    public WidgetMealInstanceHolder(View view) {
      ButterKnife.bind(this, view);
    }

    public void render(@NonNull MealInstance mealInstance) {

    }
  }
}

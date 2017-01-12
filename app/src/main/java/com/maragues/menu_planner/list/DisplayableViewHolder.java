package com.maragues.menu_planner.list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.recipe.editor.EditRecipeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by maragues on 05/12/2016.
 */

public class DisplayableViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.header_holder)
  ViewGroup headerHolder;

  @BindView(R.id.header_date)
  TextView dateTextView;

  @BindView(R.id.menu_root)
  LinearLayout root;

  @BindView(R.id.menu_recipe)
  TextView recipe;

  MenuRecyclerViewAdapter.Displayable displayable;

  public DisplayableViewHolder(View itemView) {
    super(itemView);

    ButterKnife.bind(this, itemView);
  }

  void setDisplayable(@NonNull MenuRecyclerViewAdapter.Displayable displayable,
                      @Nullable MenuRecyclerViewAdapter.Displayable previousDisplayable) {
//TODO if different dates, show header
    this.displayable = displayable;

    showHeaderIfNeeded(previousDisplayable);

    onNewDisplayable();
  }

  private void onNewDisplayable() {
    recipe.setText(displayable.title());
    for (String string : displayable.recipes()) {
      recipe.append(" " + string);
    }
  }

  private void showHeaderIfNeeded(@Nullable MenuRecyclerViewAdapter.Displayable previousDisplayable) {
    if (previousDisplayable == null
            || previousDisplayable.date().isBefore(displayable.date())) {
      headerHolder.setVisibility(View.VISIBLE);

      if (displayable != null)
        dateTextView.setText(displayable.date().toString());
    } else {
      headerHolder.setVisibility(View.GONE);
    }
  }

  @OnClick(R.id.header_add)
  void onAddClicked() {
    headerHolder.getContext().startActivity(new Intent(headerHolder.getContext(), EditRecipeActivity.class));
  }
}

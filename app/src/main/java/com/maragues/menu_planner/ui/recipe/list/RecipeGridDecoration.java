package com.maragues.menu_planner.ui.recipe.list;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by miguelaragues on 26/1/17.
 */

public class RecipeGridDecoration extends RecyclerView.ItemDecoration {
  private int offset;

  public RecipeGridDecoration(int offset) {
    this.offset = offset;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view,
                             RecyclerView parent, RecyclerView.State state) {
    outRect.left = offset/2;
    outRect.right = offset/2;
    outRect.bottom = offset;
    if (parent.getChildAdapterPosition(view) == 0) {
//      outRect.top = offset;
    }
  }
}

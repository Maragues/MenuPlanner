package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

/**
 * Created by miguelaragues on 5/1/17.
 */
public interface IRecipeList extends IBaseLoggedInView{
  void startAddRecipeActivity();

  @CallOnMainThread
  @DistinctUntilChanged
  void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter);

  void startRecipeViewer(@NonNull Recipe recipe);

  void showIsLoading(boolean isLoading);
}

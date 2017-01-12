package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

import java.util.List;

/**
 * Created by miguelaragues on 5/1/17.
 */
public interface IRecipeList extends IBaseLoggedInView{
  void startAddRecipeActivity();

  @CallOnMainThread
  @DistinctUntilChanged
  void setRecipes(List<Recipe> recipes);

  void setAdapter(RecyclerView.Adapter<RecipeListAdapter.ViewHolder> adapter);

  void startRecipeViewer(@NonNull Recipe recipe);
}

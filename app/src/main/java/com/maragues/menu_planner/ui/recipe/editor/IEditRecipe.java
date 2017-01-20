package com.maragues.menu_planner.ui.recipe.editor;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

import java.util.List;

/**
 * Created by miguelaragues on 3/1/17.
 */
public interface IEditRecipe {

  interface View extends IBaseLoggedInView, Info, Ingredients, Instructions {
    @CallOnMainThread
    void finish();

    void storeResult(Recipe recipe);
  }

  interface Info {
    String title();

    String description();

    String url();

    @CallOnMainThread
    void setTitle(String title);

    @CallOnMainThread
    void setDescription(String description);

    @CallOnMainThread
    void setUrl(String url);

    @CallOnMainThread
    void showTitleMissingError();

    @CallOnMainThread
    void showWrongUrlError();
  }

  interface Ingredients {
    List<String> ingredients();

    @CallOnMainThread
    void setIngredients(List<String> ingredients);
  }

  interface Instructions {
    List<String> steps();

    @CallOnMainThread
    void setSteps(List<String> steps);
  }
}

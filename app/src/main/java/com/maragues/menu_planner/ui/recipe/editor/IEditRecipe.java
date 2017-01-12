package com.maragues.menu_planner.ui.recipe.editor;

import com.maragues.menu_planner.ui.IBaseLoggedInView;

import java.util.List;

/**
 * Created by miguelaragues on 3/1/17.
 */
public interface IEditRecipe {

  interface View extends IBaseLoggedInView, Info, Ingredients, Instructions {
    void finish();
  }

  interface Info {
    String title();

    String description();

    String url();

    void setTitle(String title);

    void setDescription(String description);

    void setUrl(String url);

    void showTitleMissingError();

    void showWrongUrlError();
  }

  interface Ingredients {
    List<String> ingredients();

    void setIngredients(List<String> ingredients);
  }

  interface Instructions {
    List<String> steps();

    void setSteps(List<String> steps);
  }
}

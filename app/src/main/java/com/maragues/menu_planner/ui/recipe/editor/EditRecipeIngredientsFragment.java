package com.maragues.menu_planner.ui.recipe.editor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.common.BaseFragment;

import java.util.List;

/**
 * Created by miguelaragues on 5/1/17.
 */

public class EditRecipeIngredientsFragment extends BaseFragment implements IEditRecipe.Ingredients {

  public static EditRecipeIngredientsFragment newInstance() {
    return new EditRecipeIngredientsFragment();
  }

  public EditRecipeIngredientsFragment() {

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflateView(inflater, container, R.layout.fragment_edit_recipe_ingredients);
  }

  @Override
  public List<String> ingredients() {
    return null;
  }

  @Override
  public void setIngredients(List<String> ingredients) {

  }
}

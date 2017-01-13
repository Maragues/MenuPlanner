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

public class EditRecipeInstructionsFragment extends BaseFragment implements IEditRecipe.Instructions {

  public static EditRecipeInstructionsFragment newInstance() {
    return new EditRecipeInstructionsFragment();
  }

  public EditRecipeInstructionsFragment() {

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflateView(inflater, container, R.layout.fragment_edit_recipe_ingredients);
  }

  @Override
  public List<String> steps() {
    return null;
  }

  @Override
  public void setSteps(List<String> steps) {

  }
}

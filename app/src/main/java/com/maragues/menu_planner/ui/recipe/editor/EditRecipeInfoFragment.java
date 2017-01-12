package com.maragues.menu_planner.ui.recipe.editor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.BaseFragment;

import butterknife.BindView;

/**
 * Created by miguelaragues on 5/1/17.
 */

public class EditRecipeInfoFragment extends BaseFragment implements IEditRecipe.Info {
  @BindView(R.id.edit_recipe_title)
  TextInputEditText titleEditText;

  @BindView(R.id.edit_recipe_description)
  TextInputEditText descriptionEditText;

  @BindView(R.id.edit_recipe_url)
  TextInputEditText urlEditText;

  @BindView(R.id.edit_recipe_title_textInputLayout)
  TextInputLayout titleTextInputLayout;

  @BindView(R.id.edit_recipe_url_textInputLayout)
  TextInputLayout urlTextInputLayout;

  static EditRecipeInfoFragment newInstance() {
    return new EditRecipeInfoFragment();
  }

  public EditRecipeInfoFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflateView(inflater, container, R.layout.fragment_edit_recipe_info);
  }

  @Override
  public String title() {
    return titleEditText.getText().toString();
  }

  @Override
  public String description() {
    return descriptionEditText.getText().toString();
  }

  @Override
  public String url() {
    return urlEditText.getText().toString();
  }

  @Override
  public void setTitle(String title) {
    titleEditText.setText(title);
  }

  @Override
  public void setDescription(String description) {
    descriptionEditText.setText(description);
  }

  @Override
  public void setUrl(String url) {
    urlEditText.setText(url);
  }

  @Override
  public void showTitleMissingError() {
    titleTextInputLayout.setError(getString(R.string.recipe_missing_title));
  }

  @Override
  public void showWrongUrlError() {
    urlTextInputLayout.setError(getString(R.string.recipe_wrong_url));
  }
}

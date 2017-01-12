package com.maragues.menu_planner.ui.recipe.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.BaseLoggedInActivity;

import java.util.List;

import butterknife.OnClick;

public class EditRecipeActivity
        extends BaseLoggedInActivity<EditRecipePresenter, IEditRecipe.View>
        implements IEditRecipe.View {

  EditRecipeInfoFragment infoFragment;
  EditRecipeIngredientsFragment ingredientsFragment;
  EditRecipeInstructionsFragment instructionsFragment;

  @NonNull
  @Override
  public EditRecipePresenter providePresenter() {
    return new EditRecipePresenter();
  }

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  private SectionsPagerAdapter sectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_recipe_tabbed);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    viewPager = (ViewPager) findViewById(R.id.container);
    viewPager.setAdapter(sectionsPagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
  }

  @OnClick(R.id.edit_recipe_save)
  void onAddRecipeClicked() {
    getPresenter().onSaveClicked();
  }

  @Override
  public String title() {
    if (infoFragment != null)
      return infoFragment.title();

    return null;
  }

  @Override
  public String description() {
    if (infoFragment != null)
      return infoFragment.description();

    return null;
  }

  @Override
  public String url() {
    if (infoFragment != null)
      return infoFragment.url();

    return null;
  }

  @Override
  public void setTitle(String title) {
    if (infoFragment != null)
      infoFragment.setTitle(title);
  }

  @Override
  public void setDescription(String description) {
    if (infoFragment != null)
      infoFragment.setDescription(description);
  }

  @Override
  public void setUrl(String url) {
    if (infoFragment != null)
      infoFragment.setUrl(url);
  }

  @Override
  public void showTitleMissingError() {
    if (infoFragment != null)
      infoFragment.showTitleMissingError();
  }

  @Override
  public void showWrongUrlError() {
    if (infoFragment != null)
      infoFragment.showWrongUrlError();
  }

  @Override
  public List<String> ingredients() {
    if (ingredientsFragment != null)
      return ingredientsFragment.ingredients();

    return null;
  }

  @Override
  public void setIngredients(List<String> ingredients) {
    if (ingredientsFragment != null)
      ingredientsFragment.setIngredients(ingredients);
  }

  @Override
  public List<String> steps() {
    if (instructionsFragment != null)
      return instructionsFragment.steps();

    return null;
  }

  @Override
  public void setSteps(List<String> steps) {
    if (instructionsFragment != null)
      instructionsFragment.setSteps(steps);
  }

  public static Intent createAddIntent(@NonNull Context context) {
    return new Intent(context, EditRecipeActivity.class);
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  class SectionsPagerAdapter extends FragmentPagerAdapter {
    static final int INFO_POSITION = 0;
    static final int INGREDIENTS_POSITION = 1;
    static final int INSTRUCTIONS_POSITION = 2;

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case INFO_POSITION:
          return EditRecipeInfoFragment.newInstance();
        case INGREDIENTS_POSITION:
          return EditRecipeIngredientsFragment.newInstance();
        case INSTRUCTIONS_POSITION:
          return EditRecipeInstructionsFragment.newInstance();
      }

      return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

      switch (position) {
        case INFO_POSITION:
          infoFragment = (EditRecipeInfoFragment) createdFragment;
          break;
        case INGREDIENTS_POSITION:
          ingredientsFragment = (EditRecipeIngredientsFragment) createdFragment;
          break;
        case INSTRUCTIONS_POSITION:
          instructionsFragment = (EditRecipeInstructionsFragment) createdFragment;
      }

      return createdFragment;
    }

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case INFO_POSITION:
          return getString(R.string.recipe_info);
        case INGREDIENTS_POSITION:
          return getString(R.string.recipe_ingredients);
        case INSTRUCTIONS_POSITION:
          return getString(R.string.recipe_instructions);
      }
      return null;
    }
  }
}

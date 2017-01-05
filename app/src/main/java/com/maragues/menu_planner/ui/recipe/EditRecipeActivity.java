package com.maragues.menu_planner.ui.recipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.BaseLoggedInActivity;

import java.util.List;

public class EditRecipeActivity
        extends BaseLoggedInActivity<EditRecipePresenter, IEditRecipe.View>
        implements IEditRecipe.View {

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

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }
    });

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == android.R.id.home) {
      getPresenter().onHomeClicked();

      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    getPresenter().onBackPressed();

    super.onBackPressed();
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
  public void showTitleMissingError() {
    if (infoFragment != null)
      infoFragment.showTitleMissingError();
  }

  @Override
  public List<String> ingredients() {
    return null;
  }

  @Override
  public void setIngredients(List<String> ingredients) {

  }

  @Override
  public List<String> steps() {
    return null;
  }

  @Override
  public void setSteps(List<String> steps) {

  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_edit_recipe_tabbed, container, false);
      TextView textView = (TextView) rootView.findViewById(R.id.section_label);
      textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
      return rootView;
    }
  }

  EditRecipeInfoFragment infoFragment;

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
        case INSTRUCTIONS_POSITION:
      }
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

      switch (position) {
        case INFO_POSITION:
          infoFragment = (EditRecipeInfoFragment) createdFragment;
        case INGREDIENTS_POSITION:
        case INSTRUCTIONS_POSITION:
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

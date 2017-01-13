package com.maragues.menu_planner.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MenuSlot;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;
import com.maragues.menu_planner.ui.planner.PlannerFragment;
import com.maragues.menu_planner.ui.recipe.list.RecipeListFragment;
import com.maragues.menu_planner.ui.tasks.TasksFragment;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.maragues.menu_planner.ui.home.HomeActivity.HomePagerAdapter.PLANNER_POSITION;
import static com.maragues.menu_planner.ui.home.HomeActivity.HomePagerAdapter.RECIPES_POSITION;
import static com.maragues.menu_planner.ui.home.HomeActivity.HomePagerAdapter.TASKS_POSITION;

public class HomeActivity extends BaseLoggedInActivity<HomePresenter, IHome>
        implements IHome {

  @BindView(R.id.bottom_navigation)
  BottomNavigationView bottomNavigationView;

  @BindView(R.id.home_viewpager)
  ViewPager viewPager;

  public static Intent createIntent(Context context) {
    return new Intent(context, HomeActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    prepareList();

    prepareViewPager();

    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      switch (item.getItemId()) {
        case R.id.action_planner:
          viewPager.setCurrentItem(PLANNER_POSITION);
          return true;
        case R.id.action_tasks:
          viewPager.setCurrentItem(TASKS_POSITION);
          return true;
        case R.id.action_recipes:
          viewPager.setCurrentItem(RECIPES_POSITION);
          return true;
      }
      return false;
    });
  }

  private void prepareViewPager() {
    HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());

    viewPager.setAdapter(pagerAdapter);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      private MenuItem prevMenuItem;

      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if (prevMenuItem != null) {
          prevMenuItem.setChecked(false);
        } else {
          bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }

        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevMenuItem = bottomNavigationView.getMenu().getItem(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  private void prepareList() {
    List<MenuSlot> recipes = new ArrayList<>();
    MenuSlot lunchToday = new MenuSlot();
    lunchToday.name = "lunch";
    lunchToday.happensAt = ZonedDateTime.now().withHour(14);
    recipes.add(lunchToday);

    MenuSlot dinnerToday = new MenuSlot();
    dinnerToday.name = "dinner";
    dinnerToday.happensAt = ZonedDateTime.now().withHour(21);
    recipes.add(dinnerToday);

    MenuSlot dinnerYesterday = new MenuSlot();
    dinnerYesterday.name = "dinner";
    dinnerYesterday.happensAt = ZonedDateTime.now().withHour(21).minusDays(1);
    recipes.add(dinnerYesterday);
    Collections.sort(recipes, (m1, m2) -> {
      if (m1.date().isBefore(m2.date()))
        return -1;
      else if (m1.date().isAfter(m2.date()))
        return 1;

      return 0;
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_week, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @NonNull
  @Override
  public HomePresenter providePresenter() {
    return new HomePresenter();
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  class HomePagerAdapter extends FragmentPagerAdapter {
    static final int PLANNER_POSITION = 0;
    static final int RECIPES_POSITION = 1;
    static final int TASKS_POSITION = 2;

    HomePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case PLANNER_POSITION:
          return PlannerFragment.newInstance();
        case RECIPES_POSITION:
          return RecipeListFragment.newInstance();
        case TASKS_POSITION:
          return TasksFragment.newInstance();
      }

      return null;
    }

    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
      Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

      switch (position) {
        case PLANNER_POSITION:
          infoFragment = (EditRecipeInfoFragment) createdFragment;
          break;
        case RECIPES_POSITION:
          ingredientsFragment = (EditRecipeIngredientsFragment) createdFragment;
          break;
        case TASKS_POSITION:
          instructionsFragment = (EditRecipeInstructionsFragment) createdFragment;
      }

      return createdFragment;
    }*/

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case PLANNER_POSITION:
          return getString(R.string.recipe_info);
        case RECIPES_POSITION:
          return getString(R.string.recipe_ingredients);
        case TASKS_POSITION:
          return getString(R.string.recipe_instructions);
      }
      return null;
    }
  }
}

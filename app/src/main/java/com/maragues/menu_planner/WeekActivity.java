package com.maragues.menu_planner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.maragues.menu_planner.list.MenuRecyclerViewAdapter;
import com.maragues.menu_planner.model.MenuSlot;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeekActivity extends AppCompatActivity {

  @BindView(R.id.menu_recyclerview)
  RecyclerView weekRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_week);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    prepareList();
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
    Collections.sort(recipes, new Comparator<MenuSlot>() {
      @Override
      public int compare(MenuSlot m1, MenuSlot m2) {
        if (m1.date().isBefore(m2.date()))
          return -1;
        else if (m1.date().isAfter(m2.date()))
          return 1;

        return 0;
      }
    });

    weekRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    weekRecyclerView.setAdapter(new MenuRecyclerViewAdapter(recipes));
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
}

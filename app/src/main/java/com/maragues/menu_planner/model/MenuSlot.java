package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import com.maragues.menu_planner.list.MenuRecyclerViewAdapter;

/**
 * Created by maragues on 05/12/2016.
 */

public class MenuSlot implements MenuRecyclerViewAdapter.Displayable {
  public String name;
  public ZonedDateTime happensAt;

  @NonNull
  @Override
  public LocalDate date() {
    return happensAt.toLocalDate();
  }

  @NonNull
  @Override
  public String title() {
    return name;
  }

  @NonNull
  @Override
  public List<String> recipes() {
    List<String> recipes = new ArrayList<>();

    recipes.add("Melón con jamón");
    recipes.add("Asado de cordero");

    return recipes;
  }
}

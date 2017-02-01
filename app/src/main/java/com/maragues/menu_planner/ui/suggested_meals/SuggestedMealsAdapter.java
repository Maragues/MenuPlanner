package com.maragues.menu_planner.ui.suggested_meals;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.RecipeMeal;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 25/1/17.
 */

public class SuggestedMealsAdapter extends RecyclerView.Adapter<SuggestedMealsAdapter.MealViewHolder> {

  private final List<Meal> meals;

  private final BehaviorSubject<Meal> recipeClickedSubject = BehaviorSubject.create();

  Observable<Meal> recipeClickedObservable() {
    return recipeClickedSubject;
  }

  SuggestedMealsAdapter(List<Meal> meals) {
    this.meals = meals;
  }

  @Override
  public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MealViewHolder(
            LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_meal_suggestion, parent, false)
    );
  }

  @Override
  public void onBindViewHolder(MealViewHolder holder, int position) {
    final Meal meal = meals.get(position);

    holder.render(meal);

    holder.itemView.setOnClickListener(ignored -> recipeClickedSubject.onNext(meal));

    if (position % 2 == 0) {
      holder.itemView.setBackgroundResource(R.drawable.sel_grid_even);
    } else {
      holder.itemView.setBackgroundResource(R.drawable.sel_grid_odd);
    }
  }

  @Override
  public int getItemCount() {
    return meals.size();
  }

  static class MealViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.meal_suggestion_recipes)
    TextView mealRecipes;

    public MealViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }

    void render(@NonNull Meal meal) {
      mealRecipes.setText(""); //clear previous content

      Iterator<RecipeMeal> recipesIterator = meal.recipeCollection().iterator();
      while (recipesIterator.hasNext()) {
        mealRecipes.append(recipesIterator.next().name());
        if (recipesIterator.hasNext()) mealRecipes.append("\n");
      }
    }
  }
}

package com.maragues.menu_planner.ui.meal.editor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 25/1/17.
 */

public class RecipeSuggesterAdapter extends RecyclerView.Adapter<RecipeSuggesterAdapter.RecipeViewHolder> {

  private final List<Recipe> recipes;

  private final BehaviorSubject<Recipe> recipeClickedSubject = BehaviorSubject.create();

  Observable<Recipe> recipeClickedObservable() {
    return recipeClickedSubject;
  }

  RecipeSuggesterAdapter(List<Recipe> recipes) {
    this.recipes = recipes;
  }

  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new RecipeViewHolder(
            LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recipe_suggestion, parent, false)
    );
  }

  @Override
  public void onBindViewHolder(RecipeViewHolder holder, int position) {
    final Recipe recipe = recipes.get(position);

    holder.render(recipe);

    holder.itemView.setOnClickListener(ignored -> recipeClickedSubject.onNext(recipe));
  }

  @Override
  public int getItemCount() {
    return recipes.size();
  }

  static class RecipeViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.recipe_suggestion_name)
    TextView recipeName;

    public RecipeViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }

    void render(@NonNull Recipe recipe) {
      recipeName.setText(recipe.name());
    }
  }
}

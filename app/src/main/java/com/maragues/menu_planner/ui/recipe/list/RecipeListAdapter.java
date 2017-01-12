package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by miguelaragues on 5/1/17.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

  private final List<Recipe> recipes;

  public RecipeListAdapter(List<Recipe> recipes) {
    this.recipes = recipes;
  }

  @Override
  public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
            LayoutInflater.from(App.appComponent.context())
                    .inflate(R.layout.item_recipe_list, parent, false)
    );
  }

  private final PublishSubject<Recipe> onClickSubject = PublishSubject.create();

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    final Recipe recipe = recipes.get(position);

    holder.itemView.setOnClickListener(v -> onClickSubject.onNext(recipe));

    holder.setRecipe(recipe);
  }

  public Disposable subscribeToClicks(@NonNull Consumer<Recipe> consumer) {
    return onClickSubject.subscribe(consumer);
  }

  @Override
  public int getItemCount() {
    return recipes.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_recipe_name)
    TextView titleTextView;

    public ViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(itemView);
    }

    public void setRecipe(@NonNull Recipe recipe) {
      titleTextView.setText(recipe.name());
    }
  }
}

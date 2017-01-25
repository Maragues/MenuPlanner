package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguelaragues on 16/1/17.
 */

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder> {
  private static final int BASE_SIZE = 7;

  private static final int TYPE_HEADER_DAY = 0;
  private static final int TYPE_BODY = 1;

  final List<MealInstance> meals;
  private final IMealSlotListener mealSlotListener;

  public PlannerAdapter(@NonNull List<MealInstance> mealList, @NonNull IMealSlotListener listener) {
    if (mealList.size() < BASE_SIZE)
      throw new IllegalArgumentException("List must have at least " + BASE_SIZE + " elements");

    this.meals = mealList;
    this.mealSlotListener = listener;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0)
      return TYPE_HEADER_DAY;

    MealInstance mealInstance = meals.get(position);

    //no recipes, we'll just print a day
    if (!mealInstance.hasRecipes())
      return TYPE_HEADER_DAY;

    return TYPE_BODY;

    /*//there's a day on top of us and there are recipes, always print a label
    int previousType = getItemViewType(position - 1);
    if (previousType == TYPE_HEADER_DAY)
      return TYPE_HEADER_LABEL;

    //there's a label on top of us, always print the body
    if (previousType == TYPE_HEADER_LABEL)

    //now we need to detect if the MealInstance to be shown belongs to a new LocalDate
    MealInstance previousMealInstance = meals.get(position - 1);
    if (mealInstance.dateTime().toLocalDate().isAfter(previousMealInstance.dateTime().toLocalDate())) {
      //it's a different day, print a day
      return TYPE_HEADER_DAY;
    } else {
      //it's another meal inside the same day, print a label
      return TYPE_HEADER_LABEL;
    }*/
  }

  @Override
  public PlannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case TYPE_HEADER_DAY:
        return new DayViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_planner_day, parent, false),
                mealSlotListener
        );
      case TYPE_BODY:
      default:
        return new BodyViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_planner_body, parent, false),
                mealSlotListener
        );
    }
  }

  @Override
  public void onBindViewHolder(PlannerViewHolder holder, int position) {
    holder.render(meals.get(position));
  }


  @Override
  public int getItemCount() {
    return BASE_SIZE + mealsWithAtLeastOneRecipe();
  }

  private int recipesInSlots = -1;

  private int mealsWithAtLeastOneRecipe() {
    if (recipesInSlots < 0) {
      int recipes = 0;

      for (int i = 0; i < meals.size(); i++) {
        recipes += meals.get(i).hasRecipes() ? 1 : 0;
      }

      recipesInSlots = recipes;
    }

    return recipesInSlots;
  }

  abstract static class PlannerViewHolder extends RecyclerView.ViewHolder {
    protected final IMealSlotListener listener;
    private MealInstance mealInstance;

    protected MealInstance getMealSlot() {
      return mealInstance;
    }

    public PlannerViewHolder(View itemView, @NonNull IMealSlotListener listener) {
      super(itemView);

      this.listener = listener;

      ButterKnife.bind(this, itemView);
    }

    void render(@NonNull MealInstance mealInstance) {
      this.mealInstance = mealInstance;
    }
  }

  static class DayViewHolder extends PlannerViewHolder {
    @BindView(R.id.planner_header_day_name)
    TextView dayTextView;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE");

    public DayViewHolder(View itemView, @NonNull IMealSlotListener listener) {
      super(itemView, listener);
    }

    @Override
    void render(@NonNull MealInstance mealInstance) {
      super.render(mealInstance);

      dayTextView.setText(mealInstance.dateTime().format(FORMATTER));
    }

    @OnClick(R.id.planner_header_day_add)
    void onAddClicked() {
      listener.onAddToDayClicked(getMealSlot());
    }
  }

  static class BodyViewHolder extends PlannerViewHolder {
    @BindView(R.id.planner_body_textview)
    TextView bodyTextView;

    @BindView(R.id.planner_header_label)
    TextView labelTextView;

    public BodyViewHolder(View itemView, @NonNull IMealSlotListener listener) {
      super(itemView, listener);
    }

    @Override
    void render(@NonNull MealInstance mealInstance) {
      super.render(mealInstance);

      Iterator<String> it = mealInstance.recipes().keySet().iterator();
      bodyTextView.setText("");
      while (it.hasNext()) {
        bodyTextView.append(mealInstance.recipes().get(it.next()).name());
      }

      labelTextView.setText(mealInstance.labelId());
    }
  }

  interface IMealSlotListener {
    void onAddToDayClicked(@NonNull MealInstance mealInstance);

    void onAddToSlotClicked(@NonNull MealInstance mealInstance);

    void onSlotClicked(@NonNull MealInstance mealInstance);
  }
}

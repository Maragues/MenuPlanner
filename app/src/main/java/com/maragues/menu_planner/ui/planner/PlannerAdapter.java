package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealSlot;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguelaragues on 16/1/17.
 */

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder> {
  private static final int BASE_SIZE = 7;
  private static final int RECIPES_IN_SLOT_SIZE_MULTIPLIER = 2;

  private static final int TYPE_HEADER_DAY = 0;
  private static final int TYPE_HEADER_LABEL = 1;
  private static final int TYPE_BODY = 2;

  final List<MealSlot> meals;
  private final IMealSlotListener mealSlotListener;

  public PlannerAdapter(@NonNull List<MealSlot> mealList, @NonNull IMealSlotListener listener) {
    if (mealList.size() < BASE_SIZE)
      throw new IllegalArgumentException("List must have at least " + BASE_SIZE + " elements");

    this.meals = mealList;
    this.mealSlotListener = listener;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0)
      return TYPE_HEADER_DAY;

    MealSlot mealSlot = meals.get(position);

    //no recipes, we'll just print a day
    if (!mealSlot.hasRecipes())
      return TYPE_HEADER_DAY;

    //there's a day on top of us and there are recipes, always print a label
    int previousType = getItemViewType(position - 1);
    if (previousType == TYPE_HEADER_DAY)
      return TYPE_HEADER_LABEL;

    //there's a label on top of us, always print the body
    if (previousType == TYPE_HEADER_LABEL)
      return TYPE_BODY;

    //now we need to detect if the MealSlot to be shown belongs to a new LocalDate
    MealSlot previousMealSlot = meals.get(position - 1);
    if (mealSlot.dateTime().toLocalDate().isAfter(previousMealSlot.dateTime().toLocalDate())) {
      //it's a different day, print a day
      return TYPE_HEADER_DAY;
    } else {
      //it's another meal inside the same day, print a label
      return TYPE_HEADER_LABEL;
    }
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
      case TYPE_HEADER_LABEL:
        return new DayViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_planner_label, parent, false),
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
    return BASE_SIZE + slotsWithAtLeastOneRecipe() * RECIPES_IN_SLOT_SIZE_MULTIPLIER;
  }

  private int recipesInSlots = -1;

  private int slotsWithAtLeastOneRecipe() {
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
    private MealSlot mealSlot;

    protected MealSlot getMealSlot() {
      return mealSlot;
    }

    public PlannerViewHolder(View itemView, @NonNull IMealSlotListener listener) {
      super(itemView);

      this.listener = listener;

      ButterKnife.bind(this, itemView);
    }

    void render(@NonNull MealSlot mealSlot) {
      this.mealSlot = mealSlot;
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
    void render(@NonNull MealSlot mealSlot) {
      super.render(mealSlot);

      dayTextView.setText(mealSlot.dateTime().format(FORMATTER));
    }

    @OnClick(R.id.planner_header_day_add)
    void onAddClicked() {
      listener.onAddToDayClicked(getMealSlot());
    }
  }

  static class LabelViewHolder extends PlannerViewHolder {
    @BindView(R.id.planner_header_label)
    TextView labelTextView;

    public LabelViewHolder(View itemView, @NonNull IMealSlotListener listener) {
      super(itemView, listener);
    }

    @Override
    void render(@NonNull MealSlot mealSlot) {
      super.render(mealSlot);
    }
  }

  static class BodyViewHolder extends PlannerViewHolder {
    @BindView(R.id.planner_body_textview)
    TextView bodyTextView;

    public BodyViewHolder(View itemView, @NonNull IMealSlotListener listener) {
      super(itemView, listener);
    }

    @Override
    void render(@NonNull MealSlot mealSlot) {
      super.render(mealSlot);
    }
  }

  interface IMealSlotListener {
    void onAddToDayClicked(@NonNull MealSlot mealSlot);

    void onAddToSlotClicked(@NonNull MealSlot mealSlot);

    void onSlotClicked(@NonNull MealSlot mealSlot);
  }
}

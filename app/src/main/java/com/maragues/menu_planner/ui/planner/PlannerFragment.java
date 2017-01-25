package com.maragues.menu_planner.ui.planner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.ui.common.BaseTiFragment;
import com.maragues.menu_planner.ui.meal.editor.MealEditorActivity;
import com.maragues.menu_planner.ui.suggested_meals.SuggestedMealsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class PlannerFragment extends BaseTiFragment<PlannerPresenter, IPlanner>
        implements IPlanner {
  private static final int CREATE_MEAL_REQUEST_CODE = 50;

  @BindView(R.id.planner_recyclerview)
  RecyclerView plannerRecyclerView;

  @BindView(R.id.planner_header)
  TextView headerTextView;

  final CompositeDisposable disposables = new CompositeDisposable();

  PlannerAdapter adapter;

  public static PlannerFragment newInstance() {
    return new PlannerFragment();
  }

  @NonNull
  @Override
  public PlannerPresenter providePresenter() {
    return new PlannerPresenter();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflateView(inflater, container, R.layout.fragment_planner);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupPlannerView();
  }

  private void setupPlannerView() {
    plannerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  @Override
  public void onResume() {
    super.onResume();

    disposables.add(getPresenter().isLoadingObservable().subscribe(this::isLoading));
    disposables.add(getPresenter().mealsObservable().subscribe(this::onNewMeals));
  }

  @Override
  public void onPause() {
    super.onPause();

    disposables.clear();
  }

  final List<MealInstance> meals = new ArrayList<>();

  private void onNewMeals(List<MealInstance> newMeals) {
    meals.clear();
    meals.addAll(newMeals);

    if (adapter == null) {
      adapter = new PlannerAdapter(meals, listListener);
    } else {
      adapter.notifyDataSetChanged();
    }

    plannerRecyclerView.setAdapter(adapter);
  }

  private void isLoading(Boolean b) {
    Toast.makeText(getActivity(), "Is loading " + b, Toast.LENGTH_SHORT).show();
  }

  private PlannerAdapter.IMealSlotListener listListener = new PlannerAdapter.IMealSlotListener() {
    @Override
    public void onAddToDayClicked(@NonNull MealInstance mealInstance) {
      getPresenter().onAddtoDayClicked(mealInstance);
    }

    @Override
    public void onAddToSlotClicked(@NonNull MealInstance mealInstance) {
      getPresenter().onAddToSlotClicked(mealInstance);
    }

    @Override
    public void onSlotClicked(@NonNull MealInstance mealInstance) {
      getPresenter().onSlotClicked(mealInstance);
    }
  };

  @Override
  public void askForMealInstanceLabel() {
    LabelDialogFragment fragment = LabelDialogFragment.newInstance();

    fragment.labelObservable().subscribe(this::onLabelSelected);

    fragment.show(getChildFragmentManager(), LabelDialogFragment.TAG);
  }

  @Override
  public void navigateToSuggestedMeals(@NonNull MealInstance mealInstance) {
    startActivityForResult(
            SuggestedMealsActivity.createIntent(getActivity(), mealInstance), CREATE_MEAL_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == CREATE_MEAL_REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        getPresenter().onMealCreated(MealEditorActivity.extractMealId(data));
      } else {
        getPresenter().onCreateMealCancelled();
      }
    }
  }

  private void onLabelSelected(MealInstanceLabel label) {
    getPresenter().onLabelSelected(label);
  }
}

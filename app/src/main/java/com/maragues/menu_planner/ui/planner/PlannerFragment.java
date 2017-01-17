package com.maragues.menu_planner.ui.planner;

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
import com.maragues.menu_planner.ui.common.BaseTiFragment;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class PlannerFragment extends BaseTiFragment<PlannerPresenter, IPlanner>
        implements IPlanner {
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

  private void onNewMeals(List<MealInstance> meals) {
    if (adapter == null) {
      adapter = new PlannerAdapter(meals, listListener);
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
}

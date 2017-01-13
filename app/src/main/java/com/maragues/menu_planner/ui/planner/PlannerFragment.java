package com.maragues.menu_planner.ui.planner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.common.BaseTiFragment;

import butterknife.BindView;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class PlannerFragment extends BaseTiFragment<PlannerPresenter, IPlanner>
        implements IPlanner {
  @BindView(R.id.menu_recyclerview)
  RecyclerView weekRecyclerView;

  public static PlannerFragment newInstance(){
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
    weekRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
  }


}

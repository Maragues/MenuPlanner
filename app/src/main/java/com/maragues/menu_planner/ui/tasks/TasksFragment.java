package com.maragues.menu_planner.ui.tasks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.ui.common.BaseTiFragment;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class TasksFragment extends BaseTiFragment<TasksPresenter, ITasks>
        implements ITasks {
  public static TasksFragment newInstance(){
    return new TasksFragment();
  }

  @NonNull
  @Override
  public TasksPresenter providePresenter() {
    return new TasksPresenter();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflateView(inflater, container, R.layout.fragment_tasks);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

}

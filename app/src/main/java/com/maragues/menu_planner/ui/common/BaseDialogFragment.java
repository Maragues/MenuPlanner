package com.maragues.menu_planner.ui.common;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by miguelaragues on 17/1/17.
 */

public abstract class BaseDialogFragment extends DialogFragment{

  private Unbinder unbinder;

  @Override
  public void onDestroyView() {
    if (unbinder != null) {
      unbinder.unbind();

      unbinder = null;
    }

    super.onDestroyView();
  }

  protected View inflateView(LayoutInflater inflater, ViewGroup container, int layoutResId) {
    View view = inflater.inflate(layoutResId, container, false);

    bindView(view);

    return view;
  }

  protected void bindView(View view) {
    if(unbinder != null)
      unbinder.unbind();

    unbinder = ButterKnife.bind(this, view);
  }
}

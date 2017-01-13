package com.maragues.menu_planner.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maragues.menu_planner.App;
import com.squareup.leakcanary.RefWatcher;

import net.grandcentrix.thirtyinch.TiFragment;
import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.TiView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by miguelaragues on 13/1/17.
 * <p>
 * Copy from BaseFragment. CompositeFragments is not ready at this moment
 */

public abstract class BaseTiFragment<P extends TiPresenter<V>, V extends TiView>
        extends TiFragment<P, V> {
  private Unbinder unbinder;

  @Override
  public void onDestroy() {
    super.onDestroy();

    RefWatcher refWatcher = App.getRefWatcher(getActivity());
    if (refWatcher != null)
      refWatcher.watch(this);
  }

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
    if (unbinder != null)
      unbinder.unbind();

    unbinder = ButterKnife.bind(this, view);
  }
}

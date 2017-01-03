package com.maragues.menu_planner.ui.recipe;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

/**
 * Created by miguelaragues on 3/1/17.
 */
public class EditRecipePresenterTest extends BasePresenterTest<IEditRecipeView, EditRecipePresenter> {

  public EditRecipePresenterTest() {
    super(IEditRecipeView.class);
  }

  @NonNull
  @Override
  protected EditRecipePresenter createPresenter() {
    return new EditRecipePresenter();
  }

  @Test
  public void validation_emptyTitleFails() {
    doReturn("").when(view).getRecipeTitle();

    assertFalse(presenter.validates());
  }

  @Test
  public void validation_contentTitleFails() {
    doReturn("dada").when(view).getRecipeTitle();

    assertTrue(presenter.validates());
  }
}
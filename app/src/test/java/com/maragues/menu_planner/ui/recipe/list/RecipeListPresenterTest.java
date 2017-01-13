package com.maragues.menu_planner.ui.recipe.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 13/1/17.
 */
public class RecipeListPresenterTest extends BasePresenterTest<IRecipeList, RecipeListPresenter> {

  DatabaseReference ref = mock(DatabaseReference.class);

  public RecipeListPresenterTest() {
    super(IRecipeList.class);

    doReturn(ref).when(presenter).recipesQuery();
  }

  @NonNull
  @Override
  protected RecipeListPresenter createPresenter() {
    return new RecipeListPresenter();
  }

  @Test
  public void onAttached_loadsRecipes() {
    verify(view).setAdapter(any(RecyclerView.Adapter.class));
  }

  @Test
  public void onAttached_doesNotFlagIsLoading() {
    verify(presenter, never()).onLoadingStateChanged(eq(true));
  }

  @Test
  public void onViewDisplayed_flagsIsLoadingWhenLoading() {
    presenter.onViewDisplayed();

    verify(view).showIsLoading(eq(true));
  }

  @Test
  public void onDataLoaded_invisible_loadingStateNotInvoked() {
    invokeOnDataLoaded();

    verify(view, never()).showIsLoading(eq(false));
  }

  @Test
  public void onDataLoaded_visible_loadingStateInvoked() {
    presenter.onViewDisplayed();

    invokeOnDataLoaded();

    verify(view).showIsLoading(eq(false));
  }

  private void invokeOnDataLoaded() {
    assertNotNull(presenter.adapter);

    try {
      Method onDataLoaded = presenter.adapter.getClass().getDeclaredMethod("onDataChanged");

      onDataLoaded.invoke(presenter.adapter);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
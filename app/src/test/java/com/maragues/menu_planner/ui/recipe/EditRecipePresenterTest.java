package com.maragues.menu_planner.ui.recipe;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
  public void validation_titleWithContentPasses() {
    doReturn("my title").when(view).getRecipeTitle();

    assertTrue(presenter.validates());
  }

  @Test
  public void attemptSave_doesNotSaveIfNotValidates() {
    doReturn(false).when(presenter).validates();

    presenter.attemptSave();

    verify(App.appComponent.recipeProvider(), never()).create(any(Recipe.class));
  }

  @Test
  public void attemptSave_SaveIfValidates() {
    ensureRecipeBasicContent();
    doReturn(true).when(presenter).validates();

    presenter.attemptSave();

    verify(App.appComponent.recipeProvider()).create(any(Recipe.class));
  }

  @Test
  public void attemptSave_UsesNameProvidedByView() {
    String expectedName = "crema de puerros";
    doReturn(expectedName).when(view).getRecipeTitle();
    doReturn(true).when(presenter).validates();

    ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

    presenter.attemptSave();

    verify(App.appComponent.recipeProvider()).create(captor.capture());

    assertEquals(expectedName, captor.getValue().name());
  }

  @Test
  public void attemptSave_UsesDescriptionProvidedByView() {
    String expectedDescription = "Partir puerros, echar bien de pera y a comer";
    doReturn(expectedDescription).when(view).getRecipeDescription();
    ensureRecipeBasicContent();
    doReturn(true).when(presenter).validates();

    presenter.attemptSave();

    ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

    verify(App.appComponent.recipeProvider()).create(captor.capture());

    assertEquals(expectedDescription, captor.getValue().description());
  }

  private void ensureRecipeBasicContent() {
    doReturn("dada").when(view).getRecipeTitle();
  }

  @Test
  public void onHomeClicked_FinishIfNotValidates() {
    doReturn(false).when(presenter).validates();

    presenter.onHomeClicked();

    verify(view).finish();
  }

  @Test
  public void onHomeClicked_FinishIfValidates() {
    ensureRecipeBasicContent();
    doReturn(true).when(presenter).validates();

    presenter.onHomeClicked();

    verify(view).finish();
  }

  @Test
  public void onHomeClicked_AttemptsSave() {
    presenter.onBackPressed();

    verify(presenter).attemptSave();
  }
}
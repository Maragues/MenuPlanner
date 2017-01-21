package com.maragues.menu_planner.ui.recipe.editor;

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
public class EditRecipePresenterTest extends BasePresenterTest<IEditRecipe.View, EditRecipePresenter> {

  public EditRecipePresenterTest() {
    super(IEditRecipe.View.class);
  }

  @NonNull
  @Override
  protected EditRecipePresenter createPresenter() {
    return new EditRecipePresenter();
  }

  /*
  VALIDATION
   */

  @Test
  public void validation_emptyTitleFails() {
    initPresenter();

    doReturn("").when(view).title();

    assertFalse(presenter.validateOrNotifyErrors());
  }

  @Test
  public void validation_titleWithContentPasses() {
    initPresenter();

    doReturn("my title").when(view).title();

    assertTrue(presenter.validateOrNotifyErrors());
  }

  @Test
  public void validation_emptyTitleShowsError() {
    initPresenter();

    presenter.validateOrNotifyErrors();

    verify(view).showTitleMissingError();
  }

  @Test
  public void validation_emptyUrlValidates() {
    initPresenter();

    ensureBasicValidation();

    assertTrue(presenter.validateOrNotifyErrors());
  }

  @Test
  public void validation_wrongUrl_doesNotValidate() {
    initPresenter();

    ensureBasicValidation();

    //the value we return doesn't make a difference, we are mocking the validation response
    doReturn("my url").when(view).url();
    doReturn(false).when(presenter).isValidUrl();

    assertFalse(presenter.validateOrNotifyErrors());
  }

  @Test
  public void validation_wrongUrl_showsError() {
    initPresenter();

    ensureBasicValidation();

    //the value we return doesn't make a difference, we are mocking the validation response
    doReturn("my url").when(view).url();
    doReturn(false).when(presenter).isValidUrl();

    presenter.validateOrNotifyErrors();

    verify(view).showWrongUrlError();
  }

  @Test
  public void validation_validUrlValidates() {
    initPresenter();

    ensureBasicValidation();

    //the value we return doesn't make a difference, we are mocking the validation response
    doReturn("my url").when(view).url();
    doReturn(true).when(presenter).isValidUrl();

    assertTrue(presenter.validateOrNotifyErrors());
  }

  @Test
  public void attemptSave_doesNotSaveIfNotValidates() {
    initPresenter();

    doReturn(false).when(presenter).validateOrNotifyErrors();

    presenter.attemptSave();

    verify(App.appComponent.recipeProvider(), never()).create(any(Recipe.class));
  }

  @Test
  public void attemptSave_SaveIfValidates() {
    initPresenter();

    ensureBasicValidation();
    doReturn(true).when(presenter).validateOrNotifyErrors();

    presenter.attemptSave();

    verify(App.appComponent.recipeProvider()).create(any(Recipe.class));
  }

  @Test
  public void attemptSave_UsesNameProvidedByView() {
    String expectedName = "crema de puerros";
    doReturn(expectedName).when(view).title();
    doReturn(true).when(presenter).validateOrNotifyErrors();

    ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

    presenter.attemptSave();

    verify(App.appComponent.recipeProvider()).create(captor.capture());

    assertEquals(expectedName, captor.getValue().name());
  }

  @Test
  public void attemptSave_UsesDescriptionProvidedByView() {
    String expectedDescription = "Partir puerros, echar bien de pera y a comer";
    doReturn(expectedDescription).when(view).description();
    ensureBasicValidation();
    doReturn(true).when(presenter).validateOrNotifyErrors();

    presenter.attemptSave();

    ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

    verify(App.appComponent.recipeProvider()).create(captor.capture());

    assertEquals(expectedDescription, captor.getValue().description());
  }

  /*
    SAVE BUTTON
   */

  @Test
  public void onSaveClicked_attemptsSave() {
    presenter.onSaveClicked();

    verify(presenter).attemptSave();
  }

  @Test
  public void onSaveClicked_doesNotFinishIfNoValidation() {
    presenter.onSaveClicked();

    verify(view, never()).finish();
  }

  @Test
  public void onSaveClicked_finishIfValidates() {
    ensureBasicValidation();

    presenter.onSaveClicked();

    verify(view).finish();
  }

  private void ensureBasicValidation() {
    doReturn("dada").when(view).title();
  }
}
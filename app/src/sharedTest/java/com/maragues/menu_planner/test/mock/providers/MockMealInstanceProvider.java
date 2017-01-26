package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockMealInstanceProvider extends MockBaseListableProvider<MealInstance>
        implements IMealInstanceProvider {

  @Override
  public Single<MealInstance> create(MealInstance clickedMealInstance) {
    return super.createInternal(clickedMealInstance);
  }

  @NonNull
  @Override
  public Flowable<List<MealInstance>> listBetween(@NonNull LocalDateTime tStartInclusive, @NonNull LocalDateTime tEndInclusive) {
    return list();
  }
}

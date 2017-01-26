package com.maragues.menu_planner.model.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.MealInstance;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by miguelaragues on 17/1/17.
 */

public interface IMealInstanceProvider extends IListableProvider<MealInstance> {
  Single<MealInstance> create(MealInstance clickedMealInstance);

  @NonNull
  Flowable<List<MealInstance>> listBetween(@NonNull LocalDateTime tStartInclusive,
                                           @NonNull LocalDateTime tEndInclusive);
}

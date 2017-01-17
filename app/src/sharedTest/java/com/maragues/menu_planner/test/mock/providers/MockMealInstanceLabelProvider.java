package com.maragues.menu_planner.test.mock.providers;

import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.model.providers.IMealInstanceLabelProvider;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockMealInstanceLabelProvider extends MockBaseListableProvider<MealInstanceLabel>
        implements IMealInstanceLabelProvider {

  public MockMealInstanceLabelProvider() {
    super();

    create(MealInstanceLabel.builder().setId(IMealInstanceLabelProvider.LUNCH_ID).build());
    create(MealInstanceLabel.builder().setId(IMealInstanceLabelProvider.DINNER_ID).build());
  }
}

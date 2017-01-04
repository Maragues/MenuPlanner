package com.maragues.menu_planner.test.dagger;

import android.content.Context;
import android.test.mock.TestMockContext;

import com.maragues.menu_planner.test.mock.MockClock;
import com.maragues.menu_planner.test.mock.MockConnectivity;
import com.maragues.menu_planner.test.mock.TestTextUtils;
import com.maragues.menu_planner.utils.Connectivity;
import com.maragues.menu_planner.utils.LocalTextUtils;

import org.threeten.bp.Clock;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.spy;


/**
 * Created by maragues on 14/04/16.
 */
@Module
public class UnitTestAppModule{

  @Provides
  @Singleton
  public Context providesContext() {
    return spy(new TestMockContext());
  }

  @Provides
  @Singleton
  public Connectivity provideConnectivity() {
    return new MockConnectivity();
  }

  @Provides
  public Clock provideClock() {
    return MockClock.clock();
  }

  @Provides
  @Singleton
  LocalTextUtils providesLocalTextUtils() {
    return new TestTextUtils();
  }
}

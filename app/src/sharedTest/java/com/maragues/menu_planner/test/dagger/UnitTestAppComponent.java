package com.maragues.menu_planner.test.dagger;


import com.maragues.menu_planner.dagger.AppComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by maragues on 14/04/16.
 */
@Singleton
@Component(
        modules = {
                UnitTestAppModule.class,
                TestProvidersModule.class
        }
)
public interface UnitTestAppComponent extends AppComponent {
}

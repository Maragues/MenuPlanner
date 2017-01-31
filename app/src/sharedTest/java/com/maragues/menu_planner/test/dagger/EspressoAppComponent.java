package com.maragues.menu_planner.test.dagger;

import com.maragues.menu_planner.dagger.AppComponent;
import com.maragues.menu_planner.dagger.PreferencesModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by miguelaragues on 28/1/17.
 */

@Singleton
@Component(
        modules = {
                EspressoAppModule.class,
                PreferencesModule.class,
                EspressoTestProvidersModule.class
        }
)
public interface EspressoAppComponent extends AppComponent {
}

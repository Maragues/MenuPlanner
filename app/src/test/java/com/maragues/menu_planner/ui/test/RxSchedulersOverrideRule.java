package com.maragues.menu_planner.ui.test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;


/**
 * This rule registers SchedulerHooks for RxJava and RxAndroid to ensure that subscriptions
 * always subscribeOn and observeOn Schedulers.immediate().
 * Warning, this rule will reset RxAndroidPlugins and RxJavaPlugins before and after each test so
 * if the application code uses RxJava plugins this may affect the behaviour of the testing method.
 * <p>
 *
 * See https://medium.com/@fabioCollini/testing-asynchronous-rxjava-code-using-mockito-8ad831a16877#.ahj5h7jmg
 * See https://github.com/fabioCollini/TestingRxJavaUsingMockito/blob/master/app/src/test/java/it/codingjam/testingrxjava/TestSchedulerRule.java
 */
public class RxSchedulersOverrideRule implements TestRule {

  @Override
  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> immediate);

        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> testScheduler);

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> testScheduler);

        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> testScheduler);

        try {
          base.evaluate();
        } finally {
          RxJavaPlugins.reset();
          RxAndroidPlugins.reset();
        }
      }
    };
  }

  private final TestScheduler testScheduler = new TestScheduler();

  private final Scheduler immediate = new Scheduler() {
    @Override
    public Worker createWorker() {
      return new ExecutorScheduler.ExecutorWorker(Runnable::run);
    }
  };

  public TestScheduler getTestScheduler() {
    return testScheduler;
  }
}
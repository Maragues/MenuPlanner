package com.maragues.menu_planner.test;

/**
 * Created by maragues on 05/05/16.
 */
public class BaseUnitTest {
/*  @Rule
  public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();*/

  /*protected final void createAppComponent() {
    App.appComponent = DaggerUnitTestAppComponent.create();
  }

  @Before
  public void setUp() {
    RetrofitService.reset();

    createAppComponent();

    doNothing().when(App.appComponent.context()).sendBroadcast(any(Intent.class));

  }*/

  /*@After
  public void tearDown() throws Exception {
    MockConnectivity.setConnectivity(true);
  }

  protected void prepareLoggedInScenario() {
    doReturn(true).when(App.appComponent.signInPreferencesProvider()).hasAuthToken();
  }

  protected <T> void throwHttpErrorOnExecute(BaseRequest<T> request, final int errorCode){
    throwHttpErrorOnExecute(request, errorCode, null);
  }

  protected <T> void throwHttpErrorOnExecute(BaseRequest<T> request,
                                             final int errorCode,
                                             final String message){
    doReturn(Observable.create(new Observable.OnSubscribe<T>() {
      @Override
      public void call(Subscriber<? super T> subscriber) {
        subscriber.onError(ExceptionFactory.httpExceptionError(errorCode, message));
      }
    })).when(request).execute();
  }*/
}

package android.test.mock;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by maragues on 13/06/16.
 */
public class TestMockContext extends MockContext {
  @Override
  public Context getApplicationContext() {
    return super.getApplicationContext();
  }

  @Override
  public Resources getResources() {
    return super.getResources();
  }

  @Override
  public Object getSystemService(String name) {
    return super.getSystemService(name);
  }
}

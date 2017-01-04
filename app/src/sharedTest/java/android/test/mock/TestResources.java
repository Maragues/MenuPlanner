package android.test.mock;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by maragues on 13/06/16.
 */
public class TestResources extends Resources {
  /**
   * Create a new Resources object on top of an existing set of assets in an
   * AssetManager.
   *
   * @param assets  Previously created AssetManager.
   * @param metrics Current display metrics to consider when
   *                selecting/computing resource values.
   * @param config  Desired device configuration to consider when
   */
  public TestResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
    super(assets, metrics, config);
  }
}

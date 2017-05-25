package com.jingyuyao.tactical.data;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TextLoader {

  private final AssetManager assetManager;

  @Inject
  TextLoader(AssetManager assetManager) {
    this.assetManager = assetManager;
    // Why the fuck is this static setter...
    I18NBundle.setExceptionOnMissingKey(false);
  }

  public String get(StringKey stringKey) {
    I18NBundle bundle = getBundle(stringKey.getBundle());
    if (stringKey.getArgs().length == 0) {
      return bundle.get(stringKey.getId());
    } else {
      return bundle.format(stringKey.getId(), stringKey.getArgs());
    }
  }

  private I18NBundle getBundle(KeyBundle keyBundle) {
    String path = keyBundle.getPath();
    // try-catch is used instead of if/else branch to increase performance since this method
    // could potentially be called many times per frame
    try {
      return assetManager.get(path, I18NBundle.class);
    } catch (GdxRuntimeException e) {
      assetManager.load(path, I18NBundle.class);
      assetManager.finishLoadingAsset(path);
      return assetManager.get(path, I18NBundle.class);
    }
  }
}

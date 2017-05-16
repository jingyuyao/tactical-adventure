package com.jingyuyao.tactical.data;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
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

  public String get(ResourceKey resourceKey) {
    I18NBundle bundle = getBundle(resourceKey.getBundle());
    if (resourceKey.getArgs().length == 0) {
      return bundle.get(resourceKey.getKey());
    } else {
      return bundle.format(resourceKey.getKey(), resourceKey.getArgs());
    }
  }

  private I18NBundle getBundle(ResourceKeyBundle resourceKeyBundle) {
    String path = resourceKeyBundle.getPath();
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

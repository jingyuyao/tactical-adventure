package com.jingyuyao.tactical.data;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.resource.MessageBundle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessageLoader {

  private final AssetManager assetManager;

  @Inject
  MessageLoader(AssetManager assetManager) {
    this.assetManager = assetManager;
    // Why the fuck is this static setter...
    I18NBundle.setExceptionOnMissingKey(false);
  }

  public String get(Message message) {
    I18NBundle bundle = getBundle(message.getBundle());
    if (message.getArgs().length == 0) {
      return bundle.get(message.getKey());
    } else {
      return bundle.format(message.getKey(), message.getArgs());
    }
  }

  private I18NBundle getBundle(MessageBundle messageBundle) {
    String path = messageBundle.getPath();
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

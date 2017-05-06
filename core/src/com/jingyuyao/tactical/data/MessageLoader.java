package com.jingyuyao.tactical.data;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
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
    if (assetManager.isLoaded(path, I18NBundle.class)) {
      return assetManager.get(path, I18NBundle.class);
    }
    assetManager.load(path, I18NBundle.class);
    assetManager.finishLoadingAsset(path);
    return assetManager.get(path, I18NBundle.class);
  }
}

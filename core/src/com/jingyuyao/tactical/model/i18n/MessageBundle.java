package com.jingyuyao.tactical.model.i18n;

/**
 * Contains the list of messages bundles available. Bundles should be placed within the assets
 * folder. This class is used instead of {@link java.util.ResourceBundle} so the view can handle
 * actually getting the messages. We don't want the model to be aware of or access any resources
 * since doing so would rely on game engine specific code. This means the view should test all the
 * resources required by the model exists.
 */
public enum MessageBundle {
  ACTION("i18n/game/action/Action"),
  ITEM("i18n/game/item/Item");

  private final String path;

  MessageBundle(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  /**
   * Create a {@link Message}.
   */
  public Message get(String key, Object... args) {
    return new Message(this, key, args);
  }
}

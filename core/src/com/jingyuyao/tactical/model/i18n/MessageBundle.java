package com.jingyuyao.tactical.model.i18n;

/**
 * This class is used instead of {@link java.util.ResourceBundle} so the view can handle
 * actually getting the messages. The model just need to provide a key (and optional args) for
 * getting the messages. The view is responsible for verifying all the required by the model exists.
 */
public class MessageBundle {

  private final String path;

  public MessageBundle(String path) {
    this.path = path;
  }

  /**
   * The absolute path to the resource file (excluding .properties extension)
   */
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

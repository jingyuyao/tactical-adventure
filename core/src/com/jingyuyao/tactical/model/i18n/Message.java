package com.jingyuyao.tactical.model.i18n;

/**
 * A reference to an i18n message.
 */
public class Message {

  private final MessageBundle bundle;
  private final String key;
  private final Object[] args;

  /**
   * See {@link MessageBundle#get(String, Object...)}.
   *
   * @param bundle the {@link MessageBundle} this message belongs to
   * @param key the key to get the message from the bundle
   * @param args used to format the returned message, see {@link java.text.MessageFormat}
   */
  Message(MessageBundle bundle, String key, Object... args) {
    this.bundle = bundle;
    this.key = key;
    this.args = args;
  }

  public MessageBundle getBundle() {
    return bundle;
  }

  public String getKey() {
    return key;
  }

  public Object[] getArgs() {
    return args;
  }
}

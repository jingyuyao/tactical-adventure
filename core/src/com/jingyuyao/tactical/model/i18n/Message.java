package com.jingyuyao.tactical.model.i18n;

import com.google.common.base.Objects;
import java.util.Arrays;

/**
 * An immutable key to an i18n message. Arguments can be provided at creation or at run time.
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

  /**
   * Return the bundle this message belongs to.
   */
  public MessageBundle getBundle() {
    return bundle;
  }

  /**
   * Return the key used to get the message from the bundle.
   */
  public String getKey() {
    return key;
  }

  /**
   * Return the args used to format the message.
   */
  public Object[] getArgs() {
    return args;
  }

  /**
   * Create a new {@link Message} from this message with the {@code newArgs}. This is useful to
   * store a {@link Message} without args in some static variable only to supply args at run time.
   */
  public Message format(Object... newArgs) {
    return new Message(bundle, key, newArgs);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message = (Message) o;
    return Objects.equal(getBundle(), message.getBundle()) &&
        Objects.equal(getKey(), message.getKey()) &&
        Arrays.equals(getArgs(), message.getArgs());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getBundle(), getKey(), getArgs());
  }
}

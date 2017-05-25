package com.jingyuyao.tactical.model.resource;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Arrays;

/**
 * An immutable string key that have optional arguments. The key is identified by a bundle and a
 * string id. Arguments can be provided at creation or at run time to create a new key. This class
 * enables referencing external assets without having the knowledge on how to load them.
 */
public class StringKey implements Serializable {

  private KeyBundle bundle;
  private String id;
  private Object[] args;

  StringKey() {
  }

  /**
   * See {@link KeyBundle#get(String, Object...)}.
   *
   * @param bundle the {@link KeyBundle} this key belongs to
   * @param id the id this key
   * @param args the arguments for the resource, optional
   */
  StringKey(KeyBundle bundle, String id, Object... args) {
    this.bundle = bundle;
    this.id = id;
    this.args = args;
  }

  /**
   * Return the bundle this key belongs to.
   */
  public KeyBundle getBundle() {
    return bundle;
  }

  /**
   * Return the id used to get the id from the bundle.
   */
  public String getId() {
    return id;
  }

  /**
   * Return the args used to format the text.
   */
  public Object[] getArgs() {
    return args;
  }

  /**
   * Return the id joined with the bundle's path using {@code /} as separator.
   */
  public String getPath() {
    return bundle.getPath() + '/' + id;
  }

  /**
   * Create a new {@link StringKey} from this key with the {@code newArgs}.
   */
  public StringKey format(Object... newArgs) {
    return new StringKey(bundle, id, newArgs);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringKey stringKey = (StringKey) o;
    return Objects.equal(getBundle(), stringKey.getBundle()) &&
        Objects.equal(getId(), stringKey.getId()) &&
        Arrays.equals(getArgs(), stringKey.getArgs());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getBundle(), getId(), Arrays.hashCode(getArgs()));
  }
}

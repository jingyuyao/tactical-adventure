package com.jingyuyao.tactical.model.resource;

import com.google.common.base.Objects;
import java.io.Serializable;

/**
 * An immutable int key. The key is identified by a path and an int id.
 */
public class IntKey implements Serializable {

  private KeyBundle bundle;
  private int id;

  IntKey() {
  }

  /**
   * See {@link KeyBundle#get(int)}.
   */
  IntKey(KeyBundle bundle, int id) {
    this.bundle = bundle;
    this.id = id;
  }

  public KeyBundle getBundle() {
    return bundle;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    IntKey intKey = (IntKey) object;
    return getId() == intKey.getId() &&
        Objects.equal(getBundle(), intKey.getBundle());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getBundle(), getId());
  }
}

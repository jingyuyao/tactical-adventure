package com.jingyuyao.tactical.model.resource;

import com.google.common.base.Objects;
import java.io.Serializable;

/**
 * A group of similar keys. Keys can be only be manufactured from an instance of this class.
 */
public class KeyBundle implements Serializable {

  private String path;

  KeyBundle() {
  }

  /**
   * Create a bundle that points to a group of keys.
   *
   * @param path the path that identifies the bundle
   */
  public KeyBundle(String path) {
    this.path = path;
  }

  /**
   * The absolute path to the bundle
   */
  public String getPath() {
    return path;
  }

  public String getPathWithExtensions() {
    return path + ".properties";
  }

  /**
   * Create a {@link StringKey}.
   */
  public StringKey get(String key, Object... args) {
    return new StringKey(this, key, args);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KeyBundle that = (KeyBundle) o;
    return Objects.equal(getPath(), that.getPath());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getPath());
  }
}

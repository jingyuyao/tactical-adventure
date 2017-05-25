package com.jingyuyao.tactical.model.resource;

import com.google.common.base.Objects;
import java.io.Serializable;

/**
 * Groups together similar keys. A group is identified by a path and an optional extension.
 * Keys can be only be manufactured from an instance of this class.
 */
public class KeyBundle implements Serializable {

  private String path;
  private String extension;

  KeyBundle() {
  }

  /**
   * Create a bundle that points to a group of keys identified by a path and an optional extension.
   */
  KeyBundle(String path, String extension) {
    this.path = path;
    this.extension = extension;
  }

  /**
   * Create a bundle where the path is prefixed with "i18n/" and extension is ".properties"
   */
  public static KeyBundle i18n(String path) {
    return new KeyBundle("i18n/" + path, "properties");
  }

  /**
   * Create a bundle where the path is prefixed with "animation/" and no extension.
   */
  public static KeyBundle animation(String path) {
    return new KeyBundle("animation/" + path, null);
  }

  /**
   * Return the path to the bundle
   */
  public String getPath() {
    return path;
  }

  /**
   * Return the path plus plus the extension (if any). Path and extension is separated by '.'
   */
  public String getPathWithExtension() {
    return extension == null ? path : path + "." + extension;
  }

  /**
   * Create a {@link StringKey}.
   */
  public StringKey get(String key, Object... args) {
    return new StringKey(this, key, args);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    KeyBundle keyBundle = (KeyBundle) object;
    return Objects.equal(getPath(), keyBundle.getPath()) &&
        Objects.equal(extension, keyBundle.extension);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getPath(), extension);
  }
}

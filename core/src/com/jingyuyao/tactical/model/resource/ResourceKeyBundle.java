package com.jingyuyao.tactical.model.resource;

import com.google.common.base.Objects;

/**
 * This class enables the delegation of getting resources to other packages. The model just need to
 * provide a key (and optional args) for getting the resources.
 */
public class ResourceKeyBundle {

  private final String path;

  public ResourceKeyBundle(String path) {
    this.path = path;
  }

  /**
   * The absolute path to the resource file (excluding .properties extension)
   */
  public String getPath() {
    return path;
  }

  public String getPathWithExtensions() {
    return path + ".properties";
  }

  /**
   * Create a {@link ResourceKey}.
   */
  public ResourceKey get(String key, Object... args) {
    return new ResourceKey(this, key, args);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceKeyBundle that = (ResourceKeyBundle) o;
    return Objects.equal(getPath(), that.getPath());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getPath());
  }
}

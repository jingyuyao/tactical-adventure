package com.jingyuyao.tactical.model.resource;

import com.google.common.base.Objects;
import java.util.Arrays;

/**
 * An immutable key to resource. The resource is identified by a path and a key. Arguments can be
 * provided at creation or at run time.
 */
public class ResourceKey {

  private final ResourceKeyBundle bundle;
  private final String key;
  private final Object[] args;

  /**
   * See {@link ResourceKeyBundle#get(String, Object...)}.
   *
   * @param bundle the {@link ResourceKeyBundle} this key belongs to
   * @param key the key to get the key from the bundle
   * @param args the arguments for the resource, optional
   */
  ResourceKey(ResourceKeyBundle bundle, String key, Object... args) {
    this.bundle = bundle;
    this.key = key;
    this.args = args;
  }

  /**
   * Return the bundle this key belongs to.
   */
  public ResourceKeyBundle getBundle() {
    return bundle;
  }

  /**
   * Return the key used to get the key from the bundle.
   */
  public String getKey() {
    return key;
  }

  /**
   * Return the args used to format the text.
   */
  public Object[] getArgs() {
    return args;
  }

  /**
   * Create a new {@link ResourceKey} from this key with the {@code newArgs}.
   */
  public ResourceKey format(Object... newArgs) {
    return new ResourceKey(bundle, key, newArgs);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceKey resourceKey = (ResourceKey) o;
    return Objects.equal(getBundle(), resourceKey.getBundle()) &&
        Objects.equal(getKey(), resourceKey.getKey()) &&
        Arrays.equals(getArgs(), resourceKey.getArgs());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getBundle(), getKey(), Arrays.hashCode(getArgs()));
  }
}

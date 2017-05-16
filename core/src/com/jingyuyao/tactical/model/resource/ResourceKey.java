package com.jingyuyao.tactical.model.resource;

import com.google.common.base.Objects;
import java.util.Arrays;

/**
 * An immutable id to resource. The resource is identified by a path and a id. Arguments can be
 * provided at creation or at run time.
 */
public class ResourceKey {

  private final ResourceKeyBundle bundle;
  private final String id;
  private final Object[] args;

  /**
   * See {@link ResourceKeyBundle#get(String, Object...)}.
   *
   * @param bundle the {@link ResourceKeyBundle} this key belongs to
   * @param id the id this key
   * @param args the arguments for the resource, optional
   */
  ResourceKey(ResourceKeyBundle bundle, String id, Object... args) {
    this.bundle = bundle;
    this.id = id;
    this.args = args;
  }

  /**
   * Return the bundle this key belongs to.
   */
  public ResourceKeyBundle getBundle() {
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
  public String getRaw() {
    return bundle.getPath() + '/' + id;
  }

  /**
   * Create a new {@link ResourceKey} from this key with the {@code newArgs}.
   */
  public ResourceKey format(Object... newArgs) {
    return new ResourceKey(bundle, id, newArgs);
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
        Objects.equal(getId(), resourceKey.getId()) &&
        Arrays.equals(getArgs(), resourceKey.getArgs());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getBundle(), getId(), Arrays.hashCode(getArgs()));
  }
}

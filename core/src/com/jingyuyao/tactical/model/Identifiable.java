package com.jingyuyao.tactical.model;

import java.util.UUID;

/**
 * Parent of all identifiable objects that exists in the world. The objects are identified by an
 * {@link UUID} which is also used in {@link #equals(Object)} and {@link #hashCode()}. Examples of
 * identifiable objects includes ships, weapons, and persons.
 */
public class Identifiable {

  /**
   * A new random {@link UUID} is generated upon initial object creation. The serialization
   * framework must ensure this value does not change across persistence.
   */
  private UUID uuid = UUID.randomUUID();

  public UUID getID() {
    return uuid;
  }

  public boolean hasID(UUID uuid) {
    return this.uuid.equals(uuid);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Identifiable that = (Identifiable) o;

    return uuid.equals(that.uuid);
  }

  @Override
  public int hashCode() {
    return uuid.hashCode();
  }
}

package com.jingyuyao.tactical.model.person;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import java.io.Serializable;

/**
 * Sentient lifeforms that could be in ships or participate in stories.
 */
public class Person implements Serializable {

  private String name;

  Person() {
  }

  /**
   * The name of this person. May not be unique.
   */
  public ResourceKey getName() {
    return ModelBundle.PERSON_NAME.get(name);
  }

  public ResourceKey getRole() {
    return ModelBundle.PERSON_ROLE.get("none");
  }
}

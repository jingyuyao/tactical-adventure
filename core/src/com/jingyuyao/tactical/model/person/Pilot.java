package com.jingyuyao.tactical.model.person;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

/**
 * A {@link Person} that can pilot a ship.
 */
public class Pilot extends Person {

  Pilot() {
  }

  @Override
  public ResourceKey getRole() {
    return ModelBundle.PERSON_ROLE.get("pilot");
  }
}

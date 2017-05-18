package com.jingyuyao.tactical.model.person;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

public class Pilot {

  private String name;

  public ResourceKey getName() {
    return ModelBundle.PERSON_NAME.get(name);
  }
}

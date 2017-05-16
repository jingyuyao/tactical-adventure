package com.jingyuyao.tactical.model.person;

import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

class BasePerson implements Person {

  private String name;

  @Override
  public ResourceKey getName() {
    return ModelBundle.PERSON_NAME.get(name);
  }
}

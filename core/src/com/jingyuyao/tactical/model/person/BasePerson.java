package com.jingyuyao.tactical.model.person;

import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.resource.ModelBundle;

class BasePerson implements Person {

  private String nameKey;

  @Override
  public Message getName() {
    return ModelBundle.PERSON_NAME.get(nameKey);
  }
}

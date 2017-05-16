package com.jingyuyao.tactical.model.person;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;

class BasePerson implements Person {

  private String nameKey;

  @Override
  public Message getName() {
    return ModelBundle.PERSON_NAME.get(nameKey);
  }
}

package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.i18n.Message;

public class Dialogue {

  private final String characterNameKey;
  private final Message message;

  public Dialogue(String characterNameKey, Message message) {
    this.characterNameKey = characterNameKey;
    this.message = message;
  }

  public String getCharacterNameKey() {
    return characterNameKey;
  }

  public Message getMessage() {
    return message;
  }
}

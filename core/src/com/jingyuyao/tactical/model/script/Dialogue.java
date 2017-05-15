package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.i18n.Message;

public class Dialogue {

  private final Message name;
  private final Message message;

  public Dialogue(Message name, Message message) {
    this.name = name;
    this.message = message;
  }

  public Message getName() {
    return name;
  }

  public Message getMessage() {
    return message;
  }
}

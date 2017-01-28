package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.CharacterData;

class EnemySave extends CharacterSave {

  private String className;
  private CharacterData data;

  String getClassName() {
    return className;
  }

  CharacterData getData() {
    return data;
  }
}

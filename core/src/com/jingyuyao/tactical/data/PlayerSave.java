package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.character.PlayerData;

class PlayerSave extends CharacterSave {

  private String className;
  private PlayerData data;

  String getClassName() {
    return className;
  }

  PlayerData getData() {
    return data;
  }
}

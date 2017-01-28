package com.jingyuyao.tactical.model.character;

public interface CharacterFactory {

  BasePlayer createBasePlayer(PlayerData data);

  PassiveEnemy createPassiveEnemy(CharacterData data);
}

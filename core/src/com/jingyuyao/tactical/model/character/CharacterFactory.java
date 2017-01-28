package com.jingyuyao.tactical.model.character;

public interface CharacterFactory {

  BasePlayer createPlayer(PlayerData data);

  PassiveEnemy createPassiveEnemy(CharacterData data);
}

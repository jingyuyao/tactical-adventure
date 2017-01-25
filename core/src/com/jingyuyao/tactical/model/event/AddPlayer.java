package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Player;

public class AddPlayer extends ObjectEvent<Player> {

  public AddPlayer(Player object) {
    super(object);
  }
}

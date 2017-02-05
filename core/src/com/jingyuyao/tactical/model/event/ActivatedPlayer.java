package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Player;

public class ActivatedPlayer extends ObjectEvent<Player> {

  public ActivatedPlayer(Player object) {
    super(object);
  }
}

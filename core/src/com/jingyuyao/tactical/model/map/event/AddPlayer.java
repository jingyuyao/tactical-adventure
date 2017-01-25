package com.jingyuyao.tactical.model.map.event;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class AddPlayer extends AbstractEvent<Player> {

  public AddPlayer(Player object) {
    super(object);
  }
}

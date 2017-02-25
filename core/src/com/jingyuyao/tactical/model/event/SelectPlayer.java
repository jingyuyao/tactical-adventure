package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Player;

public class SelectPlayer extends SelectCharacter<Player> {

  public SelectPlayer(Player player) {
    super(player);
  }
}

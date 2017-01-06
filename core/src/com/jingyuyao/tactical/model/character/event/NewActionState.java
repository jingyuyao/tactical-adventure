package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class NewActionState extends AbstractEvent<Player> {

  private final boolean actionable;

  public NewActionState(Player player, boolean actionable) {
    super(player);
    this.actionable = actionable;
  }

  public boolean isActionable() {
    return actionable;
  }
}

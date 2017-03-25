package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

public class BasePlayer extends AbstractCharacter implements Player {

  private boolean actionable;

  BasePlayer(
      String name, int maxHp, int hp, int moveDistance, List<Item> items, boolean actionable) {
    super(name, maxHp, hp, moveDistance, items);
    this.actionable = actionable;
  }

  @Override
  public boolean isActionable() {
    return actionable;
  }

  @Override
  public void setActionable(boolean actionable) {
    this.actionable = actionable;
  }
}

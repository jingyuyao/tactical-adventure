package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.item.Item;
import java.util.List;

public class BasePlayerData extends CharacterData {

  private boolean actionable;

  boolean isActionable() {
    return actionable;
  }

  void setActionable(boolean actionable) {
    this.actionable = actionable;
  }

  @Override
  public Character load(CharacterFactory factory, List<Item> items) {
    return factory.create(this, items);
  }
}

package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.item.Item;
import java.util.List;
import javax.inject.Inject;

public class BasePlayer extends AbstractCharacter implements Player {

  private boolean actionable;

  @Inject
  BasePlayer(@CharacterEventBus EventBus eventBus) {
    super(eventBus);
  }

  BasePlayer(
      EventBus eventBus, String name, int maxHp, int hp, int moveDistance, List<Item> items,
      boolean actionable) {
    super(eventBus, name, maxHp, hp, moveDistance, items);
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

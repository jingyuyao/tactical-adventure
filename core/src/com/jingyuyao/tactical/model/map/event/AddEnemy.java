package com.jingyuyao.tactical.model.map.event;

import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.event.AbstractEvent;

public class AddEnemy extends AbstractEvent<Enemy> {

  public AddEnemy(Enemy object) {
    super(object);
  }
}

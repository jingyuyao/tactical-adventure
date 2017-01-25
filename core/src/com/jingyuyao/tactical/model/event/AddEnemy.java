package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Enemy;

public class AddEnemy extends AbstractEvent<Enemy> {

  public AddEnemy(Enemy object) {
    super(object);
  }
}

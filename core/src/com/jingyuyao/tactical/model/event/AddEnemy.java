package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Enemy;

public class AddEnemy extends ObjectEvent<Enemy> {

  public AddEnemy(Enemy object) {
    super(object);
  }
}

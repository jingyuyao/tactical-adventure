package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Enemy;

public class ActivatedEnemy extends ObjectEvent<Enemy> {

  public ActivatedEnemy(Enemy object) {
    super(object);
  }
}
package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Enemy;

public class SelectEnemy extends SelectCharacter<Enemy> {

  public SelectEnemy(Enemy object) {
    super(object);
  }
}

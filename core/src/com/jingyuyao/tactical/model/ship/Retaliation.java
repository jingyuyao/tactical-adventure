package com.jingyuyao.tactical.model.ship;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.world.Path;

public class Retaliation {

  private final Path path;
  private final Battle battle;

  Retaliation(Path path, Battle battle) {
    this.path = path;
    this.battle = battle;
  }

  public Optional<Path> path() {
    return Optional.fromNullable(path);
  }

  public Optional<Battle> battle() {
    return Optional.fromNullable(battle);
  }
}

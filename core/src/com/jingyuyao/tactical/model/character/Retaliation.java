package com.jingyuyao.tactical.model.character;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.battle.Battle2;
import com.jingyuyao.tactical.model.world.Path;

public class Retaliation {

  private final Path path;
  private final Battle2 battle2;

  Retaliation(Path path, Battle2 battle2) {
    this.path = path;
    this.battle2 = battle2;
  }

  public Optional<Path> getPath() {
    return Optional.fromNullable(path);
  }

  public Optional<Battle2> getBattle2() {
    return Optional.fromNullable(battle2);
  }
}

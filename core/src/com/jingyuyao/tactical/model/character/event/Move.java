package com.jingyuyao.tactical.model.character.event;

import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.FutureEvent;
import com.jingyuyao.tactical.model.map.Path;

public class Move extends FutureEvent<Character> {

  private final Path path;

  public Move(Character character, SettableFuture<Void> future, Path path) {
    super(character, future);
    this.path = path;
  }

  public Path getPath() {
    return path;
  }
}

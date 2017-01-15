package com.jingyuyao.tactical.model.character.event;

import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.Path;

public class Move extends AbstractEvent<Character> {

  private final Path path;
  private final SettableFuture<Void> future;

  public Move(Character character, Path path, SettableFuture<Void> future) {
    super(character);
    this.path = path;
    this.future = future;
  }

  public Path getPath() {
    return path;
  }

  public void done() {
    future.set(null);
  }
}

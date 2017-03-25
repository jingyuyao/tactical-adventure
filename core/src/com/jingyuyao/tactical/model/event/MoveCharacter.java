package com.jingyuyao.tactical.model.event;

import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Path;

public class MoveCharacter {

  private final Character character;
  private final Path path;
  private final SettableFuture<Void> future;

  public MoveCharacter(Character character, Path path, SettableFuture<Void> future) {
    this.character = character;
    this.path = path;
    this.future = future;
  }

  public Character getCharacter() {
    return character;
  }

  public Path getPath() {
    return path;
  }

  public SettableFuture<Void> getFuture() {
    return future;
  }
}

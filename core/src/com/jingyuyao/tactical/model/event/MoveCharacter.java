package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Path;

public class MoveCharacter {

  private final Character character;
  private final Path path;
  private final MyFuture future;

  public MoveCharacter(Character character, Path path, MyFuture future) {
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

  public MyFuture getFuture() {
    return future;
  }
}

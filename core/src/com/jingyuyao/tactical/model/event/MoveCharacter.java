package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Path;

public class MoveCharacter {

  private final Character character;
  private final Path path;
  private final Promise promise;

  public MoveCharacter(Character character, Path path, Promise promise) {
    this.character = character;
    this.path = path;
    this.promise = promise;
  }

  public Character getCharacter() {
    return character;
  }

  public Path getPath() {
    return path;
  }

  public Promise getPromise() {
    return promise;
  }
}

package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.Path;

public class Move extends AbstractEvent<Character> {

  private final Path path;

  public Move(Character character, Path path) {
    super(character);
    this.path = path;
  }

  public Path getPath() {
    return path;
  }
}

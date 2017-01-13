package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.Path;

public class Move extends AbstractEvent<Character> {

  private final Path path;
  private final Runnable onFinish;

  public Move(Character character, Path path, Runnable onFinish) {
    super(character);
    this.path = path;
    this.onFinish = onFinish;
  }

  public Path getPath() {
    return path;
  }

  /**
   * Indicate the move has finished.
   */
  public void finish() {
    onFinish.run();
  }
}

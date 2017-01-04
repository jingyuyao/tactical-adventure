package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.map.Terrain;

public class HighlightCharacter extends ObjectEvent<Character> {
  private final Terrain terrain;

  public HighlightCharacter(Character character, Terrain terrain) {
    super(character);
    this.terrain = terrain;
  }

  public Terrain getTerrain() {
    return terrain;
  }
}

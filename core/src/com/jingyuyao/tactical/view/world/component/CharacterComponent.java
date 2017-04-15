package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.jingyuyao.tactical.model.character.Character;

public class CharacterComponent implements Component, Poolable {

  private Character character;

  public Character getCharacter() {
    return character;
  }

  public void setCharacter(Character character) {
    this.character = character;
  }

  @Override
  public void reset() {
    character = null;
  }
}

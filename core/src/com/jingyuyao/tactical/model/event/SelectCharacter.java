package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class SelectCharacter<T extends Character> extends SelectObject<T> {

  SelectCharacter(T object) {
    super(object);
  }
}

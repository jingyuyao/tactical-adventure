package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;

class CharacterOverviewPanel extends TextPanel<Character> {

  private static final String FMT = "%s\nHP: %d";

  @Override
  String createText(Character character) {
    return String.format(Locale.US, FMT, character.getName(), character.getHp());
  }
}

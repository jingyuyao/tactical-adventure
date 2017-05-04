package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.character.Character;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Singleton;

@Singleton
class CharacterStatsPanel extends VisTable {

  CharacterStatsPanel() {
    super(true);
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Character character) {
    clearChildren();
    addText("Name");
    addText(character.getName());
    row();
    addText("HP");
    addText(String.valueOf(character.getHp()));
    row();
    addText("Move");
    addText(String.valueOf(character.getMoveDistance()));
  }

  private void addText(String text) {
    add(new VisLabel(text));
  }
}

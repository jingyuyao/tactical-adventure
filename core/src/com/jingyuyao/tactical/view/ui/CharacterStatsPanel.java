package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Character;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterStatsPanel extends VisTable {

  private final MessageLoader messageLoader;

  @Inject
  CharacterStatsPanel(MessageLoader messageLoader) {
    super(true);
    this.messageLoader = messageLoader;
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Character character) {
    clearChildren();
    addText(messageLoader.get(UIBundle.CHARACTER_NAME_HEADER));
    addText(messageLoader.get(character.getName()));
    row();
    addText(messageLoader.get(UIBundle.CHARACTER_HP_HEADER));
    addText(String.valueOf(character.getHp()));
    row();
    addText(messageLoader.get(UIBundle.CHARACTER_MOVE_HEADER));
    addText(String.valueOf(character.getMoveDistance()));
  }

  private void addText(String text) {
    add(new VisLabel(text));
  }
}

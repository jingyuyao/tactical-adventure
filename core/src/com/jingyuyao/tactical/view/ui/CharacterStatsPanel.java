package com.jingyuyao.tactical.view.ui;

import static com.jingyuyao.tactical.view.ui.GameUIModule.BUNDLE;

import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterStatsPanel extends VisTable {

  private static final Message NAME_HEADER = BUNDLE.get("characterNameHeader");
  private static final Message HP_HEADER = BUNDLE.get("characterHPHeader");
  private static final Message MOVE_HEADER = BUNDLE.get("characterMoveHeader");

  private final MessageLoader messageLoader;

  @Inject
  CharacterStatsPanel(MessageLoader messageLoader) {
    super(true);
    this.messageLoader = messageLoader;
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Character character) {
    clearChildren();
    addText(messageLoader.get(NAME_HEADER));
    addText(messageLoader.get(character.getName()));
    row();
    addText(messageLoader.get(HP_HEADER));
    addText(String.valueOf(character.getHp()));
    row();
    addText(messageLoader.get(MOVE_HEADER));
    addText(String.valueOf(character.getMoveDistance()));
  }

  private void addText(String text) {
    add(new VisLabel(text));
  }
}

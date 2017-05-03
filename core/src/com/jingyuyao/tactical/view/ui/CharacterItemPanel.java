package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Item;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Singleton;

@Singleton
class CharacterItemPanel extends VisTable {

  CharacterItemPanel() {
    super(true);
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Character character) {
    clearChildren();
    addText("Item");
    addText("Durability");
    addText("Description");
    row();

    addItems(character.getEquippedArmors());
    addItems(character.getUnequippedArmors());
    addItems(character.getWeapons());
    addItems(character.getConsumables());
    add();
    add();
    add().expand();
  }

  private void addItems(Iterable<? extends Item> items) {
    for (Item item : items) {
      addText(item.getName());
      addText(String.valueOf(item.getUsageLeft()));
      addText(item.getDescription());
      row();
    }
  }

  private void addText(String text) {
    add(new VisLabel(text));
  }
}

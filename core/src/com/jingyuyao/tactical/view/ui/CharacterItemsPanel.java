package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Item;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Singleton;

@Singleton
class CharacterItemsPanel extends VisTable {

  CharacterItemsPanel() {
    super(true);
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Character character) {
    clearChildren();
    addText("Item");
    addText("Durability");
    addText("Description");
    addText("Action");
    row();

    addItemsNoAction(character.getEquippedArmors());
    addUnequippedArmors(character);
    addItemsNoAction(character.getWeapons());
    addItemsNoAction(character.getConsumables());
    add();
    add();
    add().expand();
  }

  private void addItemsNoAction(Iterable<? extends Item> items) {
    for (Item item : items) {
      addItem(item);
      add(); // no action
      row();
    }
  }

  private void addUnequippedArmors(Character character) {
    for (Armor armor : character.getUnequippedArmors()) {
      addItem(armor);
      add(this.new EquipArmor(character, armor));
      row();
    }
  }

  private void addItem(Item item) {
    addText(item.getName());
    addText(String.valueOf(item.getUsageLeft()));
    addText(item.getDescription());
  }

  private void addText(String text) {
    add(new VisLabel(text));
  }

  private class EquipArmor extends VisTextButton {

    private EquipArmor(final Character character, final Armor armor) {
      super("Equip", new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          character.equipArmor(armor);
          // refresh
          CharacterItemsPanel.this.display(character);
        }
      });
      // TODO: polish this
      if (!(Player.class.isInstance(character) && Player.class.cast(character).isActionable())) {
        setDisabled(true);
      }
    }
  }
}

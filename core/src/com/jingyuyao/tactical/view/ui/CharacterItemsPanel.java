package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Item;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterItemsPanel extends VisTable {

  private final MessageLoader messageLoader;

  @Inject
  CharacterItemsPanel(MessageLoader messageLoader) {
    super(true);
    this.messageLoader = messageLoader;
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Character character) {
    clearChildren();
    addText(messageLoader.get(UIBundle.ITEM_NAME_HEADER));
    addText(messageLoader.get(UIBundle.ITEM_DURABILITY_HEADER));
    addText(messageLoader.get(UIBundle.ITEM_DESCRIPTION_HEADER));
    addText(messageLoader.get(UIBundle.ITEM_ACTION_HEADER));
    row();

    addEquippedArmors(character);
    addStashedArmors(character);
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

  private void addEquippedArmors(Character character) {
    for (Armor armor : character.getEquippedArmors()) {
      addItem(armor);
      add(this.new UnequipArmor(character, armor));
      row();
    }
  }

  private void addStashedArmors(Character character) {
    for (Armor armor : character.getStashedArmors()) {
      addItem(armor);
      add(this.new EquipArmor(character, armor));
      row();
    }
  }

  private void addItem(Item item) {
    addText(messageLoader.get(item.getName()));
    addText(String.valueOf(item.getUsageLeft()));
    addText(messageLoader.get(item.getDescription()));
  }

  private void addText(String text) {
    add(new VisLabel(text));
  }

  private class EquipArmor extends VisTextButton {

    private EquipArmor(final Character character, final Armor armor) {
      super(messageLoader.get(UIBundle.EQUIP_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          character.equipArmor(armor);
          // refresh
          CharacterItemsPanel.this.display(character);
        }
      });
      setDisabled(!character.canControl());
    }
  }

  private class UnequipArmor extends VisTextButton {

    private UnequipArmor(final Character character, final Armor armor) {
      super(messageLoader.get(UIBundle.UNEQUIP_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          character.unequipArmor(armor);
          // refresh
          CharacterItemsPanel.this.display(character);
        }
      });
      setDisabled(!character.canControl());
    }
  }
}

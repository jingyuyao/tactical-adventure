package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipItemsPanel extends VisTable {

  private final TextLoader textLoader;

  @Inject
  ShipItemsPanel(TextLoader textLoader) {
    super(true);
    this.textLoader = textLoader;
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Ship ship) {
    clearChildren();
    addText(UIBundle.ITEM_NAME_HEADER);
    addText(UIBundle.ITEM_DURABILITY_HEADER);
    addText(UIBundle.ITEM_DESCRIPTION_HEADER);
    addText(UIBundle.ITEM_ACTION_HEADER);
    row();

    addEquippedArmors(ship);
    addStashedArmors(ship);
    addItemsNoAction(ship.getWeapons());
    addItemsNoAction(ship.getConsumables());
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

  private void addEquippedArmors(Ship ship) {
    for (Armor armor : ship.getEquippedArmors()) {
      addItem(armor);
      add(this.new UnequipArmor(ship, armor));
      row();
    }
  }

  private void addStashedArmors(Ship ship) {
    for (Armor armor : ship.getStashedArmors()) {
      addItem(armor);
      add(this.new EquipArmor(ship, armor));
      row();
    }
  }

  private void addItem(Item item) {
    addText(item.getName());
    addText(item.getUsageLeft());
    addText(item.getDescription());
  }

  private void addText(ResourceKey resourceKey) {
    add(new VisLabel(textLoader.get(resourceKey)));
  }

  private void addText(int number) {
    add(new VisLabel(String.valueOf(number)));
  }

  private class EquipArmor extends VisTextButton {

    private EquipArmor(final Ship ship, final Armor armor) {
      super(textLoader.get(UIBundle.EQUIP_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          ship.equipArmor(armor);
          // refresh
          ShipItemsPanel.this.display(ship);
        }
      });
      setDisabled(!ship.isControllable());
    }
  }

  private class UnequipArmor extends VisTextButton {

    private UnequipArmor(final Ship ship, final Armor armor) {
      super(textLoader.get(UIBundle.UNEQUIP_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          ship.unequipArmor(armor);
          // refresh
          ShipItemsPanel.this.display(ship);
        }
      });
      setDisabled(!ship.isControllable());
    }
  }
}

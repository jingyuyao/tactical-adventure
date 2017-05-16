package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.ship.Ship;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipStatsPanel extends VisTable {

  private final MessageLoader messageLoader;

  @Inject
  ShipStatsPanel(MessageLoader messageLoader) {
    super(true);
    this.messageLoader = messageLoader;
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Ship ship) {
    clearChildren();
    addText(messageLoader.get(UIBundle.SHIP_NAME_HEADER));
    addText(messageLoader.get(ship.getName()));
    row();
    addText(messageLoader.get(UIBundle.SHIP_HP_HEADER));
    addText(String.valueOf(ship.getHp()));
    row();
    addText(messageLoader.get(UIBundle.SHIP_MOVE_HEADER));
    addText(String.valueOf(ship.getMoveDistance()));
  }

  private void addText(String text) {
    add(new VisLabel(text));
  }
}

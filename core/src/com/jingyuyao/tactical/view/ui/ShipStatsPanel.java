package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.resource.Message;
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
    addText(UIBundle.SHIP_NAME_HEADER);
    addText(ship.getName());
    row();
    addText(UIBundle.SHIP_HP_HEADER);
    addText(ship.getHp());
    row();
    addText(UIBundle.SHIP_MOVE_HEADER);
    addText(ship.getMoveDistance());
  }

  private void addText(Message message) {
    add(new VisLabel(messageLoader.get(message)));
  }

  private void addText(int number) {
    add(new VisLabel(String.valueOf(number)));
  }
}

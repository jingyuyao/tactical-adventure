package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipStatsPanel extends VisTable {

  private final TextLoader textLoader;

  @Inject
  ShipStatsPanel(TextLoader textLoader) {
    super(true);
    this.textLoader = textLoader;
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

  private void addText(StringKey stringKey) {
    add(new VisLabel(textLoader.get(stringKey)));
  }

  private void addText(int number) {
    add(new VisLabel(String.valueOf(number)));
  }
}

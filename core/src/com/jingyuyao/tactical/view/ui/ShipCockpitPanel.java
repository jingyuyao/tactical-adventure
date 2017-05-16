package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipCockpitPanel extends VisTable {

  private final TextLoader textLoader;

  @Inject
  ShipCockpitPanel(TextLoader textLoader) {
    this.textLoader = textLoader;
    defaults().top().left().pad(0, 0, 10, 10);
  }

  void display(Ship ship) {
    clearChildren();
    addText(UIBundle.PILOT_NAME_HEADER);
    row();
    for (Person pilot : ship.getPilots()) {
      addText(pilot.getName());
      row();
    }
  }

  private void addText(ResourceKey resourceKey) {
    add(new VisLabel(textLoader.get(resourceKey)));
  }
}

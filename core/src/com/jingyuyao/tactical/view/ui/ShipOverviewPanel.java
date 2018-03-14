package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.person.Pilot;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipOverviewPanel extends ButtonPanel<Ship> {

  private final ShipDetailLayer shipDetailLayer;
  private final TextLoader textLoader;

  @Inject
  ShipOverviewPanel(
      ShipDetailLayer shipDetailLayer,
      TextLoader textLoader) {
    super(Align.right);
    this.shipDetailLayer = shipDetailLayer;
    this.textLoader = textLoader;
  }

  @Override
  Optional<String> createText(Ship ship) {
    StringBuilder builder = new StringBuilder();

    String name = textLoader.get(ship.getName());
    int hp = ship.getHp();
    StringKey panelKey = UIBundle.OVERVIEW_PANEL.format(name, hp);
    builder.append(textLoader.get(panelKey));

    for (Person person : ship.getCrew()) {
      if (Pilot.class.isInstance(person)) {
        StringKey pilotKey = UIBundle.OVERVIEW_PANEL_PILOT.format(textLoader.get(person.getName()));
        builder.append(textLoader.get(pilotKey));
      }
    }

    return Optional.of(builder.toString());
  }

  @Override
  void click(Ship ship) {
    shipDetailLayer.display(ship);
  }
}

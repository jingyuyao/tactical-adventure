package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.resource.ResourceKey;
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
    if (ship.getHp() <= 0) {
      return Optional.absent();
    }
    String name = textLoader.get(ship.getName());
    int hp = ship.getHp();
    ResourceKey resourceKey = UIBundle.OVERVIEW_PANEL.format(name, hp);
    return Optional.of(textLoader.get(resourceKey));
  }

  @Override
  void click(Ship ship) {
    shipDetailLayer.display(ship);
  }
}

package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.ship.Ship;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipOverviewPanel extends ButtonPanel<Ship> {

  private final ShipDetailLayer shipDetailLayer;
  private final MessageLoader messageLoader;

  @Inject
  ShipOverviewPanel(
      ShipDetailLayer shipDetailLayer,
      MessageLoader messageLoader) {
    super(Align.right);
    this.shipDetailLayer = shipDetailLayer;
    this.messageLoader = messageLoader;
  }

  @Override
  Optional<String> createText(Ship ship) {
    if (ship.getHp() <= 0) {
      return Optional.absent();
    }
    String name = messageLoader.get(ship.getName());
    int hp = ship.getHp();
    Message message = UIBundle.OVERVIEW_PANEL.format(name, hp);
    return Optional.of(messageLoader.get(message));
  }

  @Override
  void click(Ship ship) {
    shipDetailLayer.display(ship);
  }
}

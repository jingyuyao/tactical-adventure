package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class TargetPanel extends TextPanel<Battling> {

  private final TextLoader textLoader;

  @Inject
  TargetPanel(TextLoader textLoader) {
    super(Align.left);
    this.textLoader = textLoader;
  }

  @Override
  Optional<String> createText(Battling battling) {
    StringBuilder builder = new StringBuilder(textLoader.get(UIBundle.TARGET_PANEL_HEADER));
    for (Cell cell : battling.getBattle().getTarget().getTargetCells()) {
      for (Ship ship : cell.ship().asSet()) {
        String name = textLoader.get(ship.getName());
        int hp = ship.getHp();
        StringKey stringKey = UIBundle.TARGET_PANEL_ITEM.format(name, hp);
        builder.append(textLoader.get(stringKey));
      }
    }
    return Optional.of(builder.toString());
  }

  @Subscribe
  void battling(Battling battling) {
    display(battling);
  }

  @Subscribe
  void exitState(ExitState exitState) {
    clearDisplay();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    clearDisplay();
  }
}

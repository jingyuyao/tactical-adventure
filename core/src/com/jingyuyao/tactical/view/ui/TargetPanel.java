package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class TargetPanel extends TextPanel<Battling> {

  private final MessageLoader messageLoader;

  @Inject
  TargetPanel(MessageLoader messageLoader) {
    super(Align.left);
    this.messageLoader = messageLoader;
  }

  @Override
  Optional<String> createText(Battling battling) {
    StringBuilder builder = new StringBuilder(messageLoader.get(UIBundle.TARGET_PANEL_HEADER));
    for (Cell cell : battling.getBattle().getTarget().getTargetCells()) {
      for (Ship ship : cell.character().asSet()) {
        String name = messageLoader.get(ship.getName());
        int hp = ship.getHp();
        Message message = UIBundle.TARGET_PANEL_ITEM.format(name, hp);
        builder.append(messageLoader.get(message));
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

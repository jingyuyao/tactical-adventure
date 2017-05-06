package com.jingyuyao.tactical.view.ui;

import static com.jingyuyao.tactical.view.ui.GameUIModule.BUNDLE;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.character.Character;
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

  private static final Message HEADER = BUNDLE.get("targetPanelHeader");

  private final MessageLoader messageLoader;

  @Inject
  TargetPanel(MessageLoader messageLoader) {
    super(Align.left);
    this.messageLoader = messageLoader;
  }

  @Override
  Optional<String> createText(Battling battling) {
    StringBuilder builder = new StringBuilder(messageLoader.get(HEADER));
    for (Cell cell : battling.getBattle().getTarget().getTargetCells()) {
      for (Character character : cell.character().asSet()) {
        String name = messageLoader.get(character.getName());
        int hp = character.getHp();
        Message message = BUNDLE.get("targetPanelItem", name, hp);
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

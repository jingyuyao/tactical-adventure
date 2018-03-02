package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.Turn;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class TurnPanel extends TextPanel<Turn> {

  private final TextLoader textLoader;

  @Inject
  TurnPanel(TextLoader textLoader) {
    super(Align.center);
    this.textLoader = textLoader;
  }

  @Override
  Optional<String> createText(Turn turn) {
    return Optional.of(textLoader.get(UIBundle.TURN.format(turn.getNumber())));
  }

  @Subscribe
  void state(State state) {
    display(state.getTurn());
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    clearDisplay();
  }
}

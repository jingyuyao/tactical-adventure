package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.State;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class ActionGroup extends VerticalGroup {

  private final MessageLoader messageLoader;

  @Inject
  ActionGroup(MessageLoader messageLoader) {
    this.messageLoader = messageLoader;
    space(25);
    columnRight();
  }

  @Subscribe
  void state(State state) {
    for (final Action action : state.getActions()) {
      String text = messageLoader.get(action.getMessage());
      VisTextButton button = new VisTextButton(text, new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          action.run();
        }
      });
      button.getLabelCell().pad(15);
      button.getLabel().setAlignment(Align.right);

      addActor(button);
    }
  }

  @Subscribe
  void exitState(ExitState exitState) {
    clear();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    clear();
  }
}

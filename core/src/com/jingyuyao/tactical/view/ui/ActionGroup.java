package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.jingyuyao.tactical.model.state.Action;
import javax.inject.Singleton;

@Singleton
class ActionGroup extends VerticalGroup {

  ActionGroup() {
    space(7);
    columnRight();
  }

  void loadActions(Iterable<Action> actions) {
    for (Action action : actions) {
      addActor(new ActionButton(action));
    }
  }
}

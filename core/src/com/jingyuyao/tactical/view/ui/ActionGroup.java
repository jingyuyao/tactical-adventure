package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.jingyuyao.tactical.model.state.Action;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActionGroup extends VerticalGroup {

  private final UIFactory uiFactory;

  @Inject
  ActionGroup(UIFactory uiFactory) {
    this.uiFactory = uiFactory;
    space(7);
    columnRight();
  }

  public void loadActions(Iterable<Action> actions) {
    for (Action action : actions) {
      addActor(uiFactory.createActionButton(action));
    }
  }
}

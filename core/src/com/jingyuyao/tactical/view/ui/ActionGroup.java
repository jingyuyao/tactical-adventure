package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.state.Action;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Singleton;

@Singleton
class ActionGroup extends VerticalGroup {

  ActionGroup() {
    space(7);
    columnRight();
  }

  void loadActions(Iterable<Action> actions) {
    for (final Action action : actions) {
      VisTextButton button = new VisTextButton(action.getName());
      button.getLabelCell().pad(10);
      Label label = button.getLabel();
      label.setAlignment(Align.right);
      label.setFontScale(0.5f);
      button.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          action.run();
        }
      });

      addActor(button);
    }
  }
}

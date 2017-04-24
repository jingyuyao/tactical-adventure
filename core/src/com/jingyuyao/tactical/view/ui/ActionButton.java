package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.state.Action;
import com.kotcrab.vis.ui.widget.VisTextButton;

class ActionButton extends VisTextButton {

  ActionButton(final Action action) {
    super(action.getName());
    getLabel().setAlignment(Align.right);
    getLabel().setFontScale(0.5f);
    getLabelCell().pad(10);
    addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            action.run();
          }
        });
  }
}

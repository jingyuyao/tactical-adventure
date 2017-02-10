package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.state.Action;
import javax.inject.Inject;

class ActionButton extends TextButton {

  @Inject
  ActionButton(@Assisted final Action action, Skin skin) {
    super(action.getName(), skin);
    getLabel().setAlignment(Align.right);
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

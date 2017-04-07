package com.jingyuyao.tactical.screen.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.GameState;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ResetButton extends TextButton {

  @Inject
  ResetButton(Skin skin, final GameState gameState, final PlayInfo playInfo) {
    super("Reset", skin);
    addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.reset();
        playInfo.updateText();
      }
    });
  }
}

package com.jingyuyao.tactical.screen.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.GameState;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class PlayButton extends TextButton {

  @Inject
  PlayButton(Skin skin, final GameState gameState) {
    super("Play", skin);
    addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        gameState.play();
      }
    });
  }
}

package com.jingyuyao.tactical.screen.play;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class PlayMenuLayout extends Table {

  @Inject
  PlayMenuLayout(
      Skin skin,
      PlayInfo playInfo,
      PlayButton playButton,
      ResetButton resetButton) {
    super(skin);
    setDebug(true);
    setFillParent(true);
    pad(10);

    add(playInfo).grow();
    row();

    Table buttonTable = new Table(skin);
    buttonTable.add(playButton).left();
    buttonTable.add().expand();
    buttonTable.add(resetButton).right();

    add(buttonTable).fill();
  }
}

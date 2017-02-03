package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EnemyBurst extends CharacterBurst {

  @Inject
  EnemyBurst(Skin skin) {
    super(skin);
    columnLeft();
  }
}

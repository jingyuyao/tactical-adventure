package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SecondaryInfo extends Info {

  @Inject
  SecondaryInfo(Skin skin) {
    super(skin);
    columnLeft();
  }
}

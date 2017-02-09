package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WeaponInfo extends VerticalGroup {

  private final Skin skin;

  @Inject
  WeaponInfo(Skin skin) {
    this.skin = skin;
    columnLeft();
    addActor(new Label("TEST\nTEST\nTEST\nTEST\n", skin));
  }
}

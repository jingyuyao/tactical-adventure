package com.jingyuyao.tactical.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.menu.play.PlayMenuModule;

public class MenuModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Input.class);
    requireBinding(GL20.class);
    requireBinding(Batch.class);
    requireBinding(Skin.class);
    requireBinding(GameState.class);
    requireBinding(DataManager.class);

    install(new PlayMenuModule());
  }
}

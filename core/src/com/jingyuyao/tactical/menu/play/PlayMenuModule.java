package com.jingyuyao.tactical.menu.play;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.menu.MenuConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class PlayMenuModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Input.class);
    requireBinding(GL20.class);
    requireBinding(Batch.class);
    requireBinding(Skin.class);
    requireBinding(MenuConfig.class);
    requireBinding(GameState.class);
    requireBinding(DataManager.class);
  }

  @Provides
  @Singleton
  @PlayMenuStage
  Stage providePlayMenuStage(MenuConfig menuConfig, Batch batch) {
    Viewport viewport =
        new StretchViewport(menuConfig.getMenuViewportWidth(), menuConfig.getMenuViewportHeight());
    return new Stage(viewport, batch);
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface PlayMenuStage {

  }
}

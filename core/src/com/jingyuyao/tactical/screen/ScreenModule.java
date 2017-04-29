package com.jingyuyao.tactical.screen;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.data.DataManager;
import com.jingyuyao.tactical.view.ui.WorldUI;
import com.jingyuyao.tactical.view.world.WorldView;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

public class ScreenModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Input.class);
    requireBinding(GL20.class);
    requireBinding(Batch.class);
    requireBinding(GameState.class);
    requireBinding(DataManager.class);
    requireBinding(WorldView.class);
    requireBinding(WorldUI.class);
    requireBinding(CameraController.class);
    requireBinding(WorldController.class);
  }

  @Provides
  @MenuScreenStage
  Stage provideMenuStage(ScreenConfig screenConfig, Batch batch) {
    int width = screenConfig.getMenuScreenWidth();
    int height = screenConfig.getMenuScreenHeight();
    return new Stage(new StretchViewport(width, height), batch);
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface MenuScreenStage {

  }
}
package com.jingyuyao.tactical.view.ui;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.GameState;
import com.jingyuyao.tactical.data.TextLoader;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Deque;
import java.util.LinkedList;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class GameUIModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Batch.class);
    requireBinding(TextLoader.class);
    requireBinding(GameState.class);
  }

  @Provides
  @Singleton
  @UIStage
  Stage provideUIStage(@UIViewport Viewport viewport, Batch batch) {
    return new Stage(viewport, batch);
  }

  @Provides
  @Singleton
  @BackingLayerStack
  Deque<Actor> provideBackingLayerStack() {
    return new LinkedList<>();
  }

  @Provides
  @Singleton
  @UIViewport
  Viewport provideUIViewport(GameUIConfig gameUiConfig) {
    return new StretchViewport(gameUiConfig.getUIWidth(), gameUiConfig.getUIHeight());
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface UIStage {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingLayerStack {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface UIViewport {

  }
}

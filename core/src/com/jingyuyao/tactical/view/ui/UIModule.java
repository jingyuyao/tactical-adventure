package com.jingyuyao.tactical.view.ui;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class UIModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Batch.class);
    requireBinding(Skin.class);

    install(new FactoryModuleBuilder().build(UIFactory.class));
  }

  @Provides
  @Singleton
  @WorldUIStage
  Stage provideWorldUIStage(@WorldUIViewport Viewport viewport, Batch batch) {
    return new Stage(viewport, batch);
  }

  @Provides
  @Singleton
  @WorldUIViewport
  Viewport provideWorldUIViewport(UIConfig uiConfig) {
    return new StretchViewport(uiConfig.getUIViewportWidth(), uiConfig.getUIViewportHeight());
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface WorldUIStage {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface WorldUIViewport {

  }
}

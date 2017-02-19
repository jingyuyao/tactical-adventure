package com.jingyuyao.tactical.view.ui;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.view.world.WorldConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class UIModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(WorldConfig.class);

    install(new FactoryModuleBuilder().build(UIFactory.class));
  }

  @Provides
  @Singleton
  Skin provideSkin(AssetManager assetManager) {
    return assetManager.get(AssetModule.SKIN, Skin.class);
  }

  @Provides
  @Singleton
  @MapUIStage
  Stage provideMapUIStage(@MapUIViewport Viewport viewport, Batch batch, RootTable rootTable) {
    Stage stage = new Stage(viewport, batch);
    stage.addActor(rootTable);
    return stage;
  }

  @Provides
  @Singleton
  @MapUIViewport
  Viewport provideMapUIViewport(UIConfig uiConfig) {
    return new StretchViewport(uiConfig.getUIViewportWidth(), uiConfig.getUIViewportHeight());
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface MapUIStage {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface MapUIViewport {

  }
}

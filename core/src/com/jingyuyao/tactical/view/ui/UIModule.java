package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.AssetModule;
import javax.inject.Singleton;

public class UIModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);

    install(new FactoryModuleBuilder().build(UIFactory.class));
  }

  @Provides
  @Singleton
  Skin provideSkin(AssetManager assetManager) {
    return assetManager.get(AssetModule.SKIN, Skin.class);
  }
}

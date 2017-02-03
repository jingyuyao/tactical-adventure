package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class UIModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Skin.class);

    install(new FactoryModuleBuilder().build(UIFactory.class));
  }
}

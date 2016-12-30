package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.view.actor.ActorModule;

import javax.inject.Singleton;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ActorModule());

        bind(LevelScreenFactory.class);
        bind(MapViewFactory.class);
        bind(MapUI.class);
    }

    @Provides
    @Singleton
    Skin provideSkin(AssetManager assetManager) {
        return assetManager.get(AssetModule.SKIN, Skin.class);
    }
}

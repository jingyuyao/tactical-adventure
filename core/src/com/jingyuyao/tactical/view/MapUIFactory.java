package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.AssetModule;
import com.jingyuyao.tactical.model.Level;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MapUIFactory {
    private final EventBus eventBus;
    private final AssetManager assetManager;

    @Inject
    MapUIFactory(EventBus eventBus, AssetManager assetManager) {
        this.eventBus = eventBus;
        this.assetManager = assetManager;
    }

    MapUI create(Level level) {
        return new MapUI(
                eventBus,
                level.getMapState(),
                assetManager.get(AssetModule.SKIN, Skin.class)
        );
    }
}

package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.model.Level;

class MapUIFactory {
    private final EventBus eventBus;
    private final AssetManager assetManager;

    MapUIFactory(EventBus eventBus, AssetManager assetManager) {
        this.eventBus = eventBus;
        this.assetManager = assetManager;
    }

    MapUI create(Level level) {
        return new MapUI(
                eventBus, level.getMapState(), level.getHighlighter(),
                level.getWaiter(),
                assetManager.get(Assets.SKIN, Skin.class)
        );
    }
}

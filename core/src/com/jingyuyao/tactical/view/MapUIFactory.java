package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.model.Level;

class MapUIFactory {
    private final AssetManager assetManager;

    MapUIFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    MapUI create(Level level) {
        return new MapUI(
                level.getMapState(), level.getHighlighter(),
                level.getAnimationCounter(),
                assetManager.get(Assets.SKIN, Skin.class)
        );
    }
}

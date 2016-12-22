package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.state.MapState;

public class MapUIFactory {
    private final AssetManager assetManager;

    public MapUIFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public MapUI create(Map map, MapState mapState) {
        return new MapUI(map, mapState, assetManager.get(Assets.SKIN, Skin.class));
    }
}

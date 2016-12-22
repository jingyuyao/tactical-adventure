package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jingyuyao.tactical.Assets;

public class MapUIFactory {
    public static MapUI create(AssetManager assetManager) {
        return new MapUI(assetManager.get(Assets.SKIN, Skin.class));
    }
}

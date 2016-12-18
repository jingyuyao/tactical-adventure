package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.model.UpdateListener;

import java.util.Map;

public class TerrainActor extends MapActor<Terrain> {
    private final Map<Terrain.SelectionMode, Sprite> selectionModeSpriteMap;

    TerrainActor(Terrain object, float size, Map<Terrain.SelectionMode, Sprite> selectionModeSpriteMap,
                 EventListener... listeners) {
        super(object, size, listeners);
        this.selectionModeSpriteMap = selectionModeSpriteMap;
    }

    @Override
    protected UpdateListener getUpdateListener() {
        return new UpdateListener() {
            @Override
            public void updated() {
                updateSelectionMode();
            }
        };
    }

    private void updateSelectionMode() {
        setSprite(selectionModeSpriteMap.get(getObject().getSelectionMode()));
    }
}

package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.model.UpdateListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class TerrainActor extends MapActor<Terrain> {
    private final Map<Terrain.Marker, Sprite> markerSpriteMap;
    private final Collection<Sprite> markerSprites;

    TerrainActor(Terrain object, float size, Map<Terrain.Marker, Sprite> markerSpriteMap,
                 EventListener... listeners) {
        super(object, size, listeners);
        this.markerSpriteMap = markerSpriteMap;
        markerSprites = new ArrayList<Sprite>();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (Sprite sprite : markerSprites) {
            sprite.setBounds(getX(), getY(), getWidth(), getHeight());
            sprite.draw(batch);
        }
    }

    @Override
    protected UpdateListener getUpdateListener() {
        return new UpdateListener() {
            @Override
            public void updated() {
                updateMarkers();
            }
        };
    }

    private void updateMarkers() {
        markerSprites.clear();
        for (Terrain.Marker marker : getObject().getMarkers()) {
            markerSprites.add(markerSpriteMap.get(marker));
        }
    }
}

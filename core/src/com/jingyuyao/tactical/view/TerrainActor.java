package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.Terrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;

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
    public void update(Observable observable, Object o) {
        updateMarkers();
    }

    private void updateMarkers() {
        markerSprites.clear();
        for (Terrain.Marker marker : getObject().getMarkers()) {
            markerSprites.add(markerSpriteMap.get(marker));
        }
    }
}

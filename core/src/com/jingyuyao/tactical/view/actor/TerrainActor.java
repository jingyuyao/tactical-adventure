package com.jingyuyao.tactical.view.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.jingyuyao.tactical.model.Markers;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class TerrainActor extends AbstractActor<Terrain> {
    private final Map<Markers, Sprite> markerSpriteMap;
    private final List<Sprite> markerSprites;

    TerrainActor(
            Terrain object,
            float size,
            Waiter waiter,
            Map<Markers, Sprite> markerSpriteMap,
            EventListener listener
    ) {
        super(object, size, waiter, listener);
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
    public void update(Observable observable, Object param) {
        if (Terrain.MarkerChange.class.isInstance(param)) {
            markerChange(Terrain.MarkerChange.class.cast(param));
        }
    }

    private void markerChange(Terrain.MarkerChange markerChange) {
        markerSprites.clear();
        for (Markers marker : markerChange.getMarkers()) {
            markerSprites.add(markerSpriteMap.get(marker));
        }
    }
}

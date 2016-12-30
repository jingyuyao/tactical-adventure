package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.event.HighlightCharacter;
import com.jingyuyao.tactical.model.event.HighlightTerrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.util.DisposableObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Highlighter extends DisposableObject {
    private final TerrainGrid terrainGrid;
    private AbstractObject previousHighlight;

    @Inject
    public Highlighter(EventBus eventBus, TerrainGrid terrainGrid) {
        super(eventBus);
        this.terrainGrid = terrainGrid;
    }

    @Override
    protected void disposed() {
        previousHighlight = null;
        super.disposed();
    }

    public void highlight(Character character) {
        setNewHighlight(character);
        getEventBus().post(new HighlightCharacter(character, terrainGrid.get(character.getCoordinate())));
    }

    public void highlight(Terrain terrain) {
        setNewHighlight(terrain);
        getEventBus().post(new HighlightTerrain(terrain));
    }

    private void setNewHighlight(AbstractObject newHighlight) {
        if (previousHighlight != null) {
            previousHighlight.removeMarker(Marker.HIGHLIGHT);
        }
        newHighlight.addMarker(Marker.HIGHLIGHT);
        previousHighlight = newHighlight;
    }

}

package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.util.DisposableObject;
import com.jingyuyao.tactical.model.util.ModelEvent;

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

    public static class HighlightCharacter implements ModelEvent {
        private final Character character;
        private final Terrain terrain;

        private HighlightCharacter(Character character, Terrain terrain) {
            this.character = character;
            this.terrain = terrain;
        }

        public Character getCharacter() {
            return character;
        }

        public Terrain getTerrain() {
            return terrain;
        }
    }

    public static class HighlightTerrain implements ModelEvent {
        private final Terrain terrain;

        private HighlightTerrain(Terrain terrain) {
            this.terrain = terrain;
        }

        public Terrain getTerrain() {
            return terrain;
        }
    }
}

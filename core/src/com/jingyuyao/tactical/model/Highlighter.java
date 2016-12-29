package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.util.ModelEvent;

public class Highlighter {
    private final EventBus eventBus;
    private final Map map;
    private AbstractObject previousHighlight;

    public Highlighter(EventBus eventBus, Map map) {
        this.eventBus = eventBus;
        this.map = map;
    }

    public void highlight(Character character) {
        setNewHighlight(character);
        eventBus.post(new HighlightCharacter(character, map.getTerrains().get(character.getCoordinate())));
    }

    public void highlight(Terrain terrain) {
        setNewHighlight(terrain);
        eventBus.post(new HighlightTerrain(terrain));
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

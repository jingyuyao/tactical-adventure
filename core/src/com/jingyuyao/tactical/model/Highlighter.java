package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

// TODO: move this to abstract state after eventbus refactor
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
        eventBus.post(new CharacterAndTerrain(character, map.getTerrains().get(character.getCoordinate())));
    }

    public void highlight(Terrain terrain) {
        setNewHighlight(terrain);
        eventBus.post(new JustTerrain(terrain));
    }

    private void setNewHighlight(AbstractObject newHighlight) {
        if (previousHighlight != null) {
            previousHighlight.removeMarker(Marker.HIGHLIGHT);
        }
        newHighlight.addMarker(Marker.HIGHLIGHT);
        previousHighlight = newHighlight;
    }

    public static class CharacterAndTerrain {
        private final Character character;
        private final Terrain terrain;

        private CharacterAndTerrain(Character character, Terrain terrain) {
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

    public static class JustTerrain {
        private final Terrain terrain;

        private JustTerrain(Terrain terrain) {
            this.terrain = terrain;
        }

        public Terrain getTerrain() {
            return terrain;
        }
    }
}

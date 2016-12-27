package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.object.AbstractObject;
import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.Observable;

public class Highlighter extends Observable {
    private final Map map;
    private AbstractObject previousHighlight;

    public Highlighter(Map map) {
        this.map = map;
    }

    public void highlight(Character character) {
        setNewHighlight(character);
        setChanged();
        notifyObservers(new CharacterAndTerrain(character, map.getTerrains().get(character.getCoordinate())));
    }

    public void highlight(Terrain terrain) {
        setNewHighlight(terrain);
        setChanged();
        notifyObservers(new JustTerrain(terrain));
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

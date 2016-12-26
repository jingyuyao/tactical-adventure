package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.Observable;

public class Highlighter extends Observable {
    private final Map map;

    public Highlighter(Map map) {
        this.map = map;
    }

    public void highlight(Character character) {
        setChanged();
        notifyObservers(new HighlightCharacter(character, map.getTerrains().get(character.getCoordinate())));
    }

    public void highlight(Terrain terrain) {
        setChanged();
        notifyObservers(new HighlightTerrain(terrain));
    }

    public static class HighlightCharacter {
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

    public static class HighlightTerrain {
        private final Terrain terrain;

        private HighlightTerrain(Terrain terrain) {
            this.terrain = terrain;
        }

        public Terrain getTerrain() {
            return terrain;
        }
    }
}

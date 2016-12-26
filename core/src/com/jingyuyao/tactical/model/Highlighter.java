package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.object.Character;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.Observable;

public class Highlighter extends Observable {
    public void highlight(Character character) {
        setChanged();
        notifyObservers(new HighlightCharacter(character));
    }

    public void highlight(Terrain terrain) {
        setChanged();
        notifyObservers(new HighlightTerrain(terrain));
    }

    public static class HighlightCharacter {
        private final Character character;

        private HighlightCharacter(Character character) {
            this.character = character;
        }

        public Character getCharacter() {
            return character;
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

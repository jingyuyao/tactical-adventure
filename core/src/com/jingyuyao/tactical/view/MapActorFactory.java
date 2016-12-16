package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jingyuyao.tactical.controller.HighlightListener;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Terrain;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Creates {@link MapActor} from models and adds the proper controllers.
 */
public class MapActorFactory {
    private final Provider<HighlightListener> highlightListenerProvider;
    private final ShapeRenderer shapeRenderer;

    @Inject
    public MapActorFactory(Provider<HighlightListener> highlightListenerProvider, ShapeRenderer shapeRenderer) {
        this.highlightListenerProvider = highlightListenerProvider;
        this.shapeRenderer = shapeRenderer;
    }

    MapActor<Character> createCharacter(Character character) {
        return new MapActor<Character>(character, highlightListenerProvider.get(), shapeRenderer);
    }

    MapActor<Terrain> createTerrain(Terrain terrain) {
        return new MapActor<Terrain>(terrain, highlightListenerProvider.get(), shapeRenderer);
    }
}

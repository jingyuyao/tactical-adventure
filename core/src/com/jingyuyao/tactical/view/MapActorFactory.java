package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private final AssetManager assetManager;

    @Inject
    public MapActorFactory(Provider<HighlightListener> highlightListenerProvider, ShapeRenderer shapeRenderer,
                           AssetManager assetManager) {
        this.highlightListenerProvider = highlightListenerProvider;
        this.shapeRenderer = shapeRenderer;
        this.assetManager = assetManager;
    }

    MapActor<Character> createCharacter(Character character) {
        MapActor<Character> actor =
                new MapActor<Character>(character, highlightListenerProvider.get(), shapeRenderer);
        Texture texture = assetManager.get("sprites/" + character.getName() + ".png", Texture.class);
        Sprite sprite = new Sprite(texture);
        actor.setSprite(sprite);
        return actor;
    }

    MapActor<Terrain> createTerrain(Terrain terrain) {
        return new MapActor<Terrain>(terrain, highlightListenerProvider.get(), shapeRenderer);
    }
}

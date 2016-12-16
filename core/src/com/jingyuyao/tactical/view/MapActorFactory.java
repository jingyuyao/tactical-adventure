package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.AssetsModule;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Terrain;

import javax.inject.Inject;

/**
 * Creates {@link MapActor} from models and adds the proper controllers.
 */
public class MapActorFactory {
    private final ControllerFactory controllerFactory;
    private final AssetManager assetManager;
    private final Sprite highlight;

    @Inject
    public MapActorFactory(ControllerFactory controllerFactory, AssetManager assetManager) {
        this.controllerFactory = controllerFactory;
        this.assetManager = assetManager;
        highlight = new Sprite(assetManager.get(AssetsModule.HIGHLIGHT, Texture.class));
    }

    MapActor createCharacter(Map map, Character character) {
        MapActor actor = new MapActor(character, highlight, controllerFactory.createHighlightController(map, character));
        actor.addListener(controllerFactory.characterController(map, character));

        Texture texture = assetManager.get("sprites/" + character.getName() + ".png", Texture.class);
        Sprite sprite = new Sprite(texture);
        actor.setSprite(sprite);

        return actor;
    }

    MapActor createTerrain(Map map, Terrain terrain) {
        MapActor actor = new MapActor(terrain, highlight, controllerFactory.createHighlightController(map, terrain));
        actor.addListener(controllerFactory.terrainController(map, terrain));

        return actor;
    }
}

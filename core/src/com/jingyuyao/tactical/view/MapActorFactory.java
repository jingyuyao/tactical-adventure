package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.AssetsModule;
import com.jingyuyao.tactical.controller.CharacterController;
import com.jingyuyao.tactical.controller.HighlightController;
import com.jingyuyao.tactical.controller.TerrainController;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Terrain;

import javax.inject.Inject;

/**
 * Creates {@link MapActor} from models and adds the proper controllers.
 */
public class MapActorFactory {
    private final AssetManager assetManager;
    private final Sprite highlight;

    @Inject
    public MapActorFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
        highlight = new Sprite(assetManager.get(AssetsModule.HIGHLIGHT, Texture.class));
    }

    MapActor createCharacter(Map map, Character character) {
        MapActor actor = new MapActor(character, highlight, new HighlightController(map, character));
        actor.addListener(new CharacterController(map, character, actor.getSize()));

        Texture texture = assetManager.get("sprites/" + character.getName() + ".png", Texture.class);
        Sprite sprite = new Sprite(texture);
        actor.setSprite(sprite);

        return actor;
    }

    MapActor createTerrain(Map map, Terrain terrain) {
        MapActor actor = new MapActor(terrain, highlight, new HighlightController(map, terrain));
        actor.addListener(new TerrainController(map, terrain, actor.getSize()));

        return actor;
    }
}

package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.*;

public class CharacterController extends MapActorController {
    private final Character character;

    public CharacterController(MapLogic map, Character character, float actorSize) {
        super(map, actorSize);
        this.character = character;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("CharacterController", character.toString());

        getMapLogic().select(character);
    }
}

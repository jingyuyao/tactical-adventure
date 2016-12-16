package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;

public class CharacterController extends InputListener {
    private final Map map;
    private final Character character;

    CharacterController(Map map, Character character) {
        this.map = map;
        this.character = character;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.log("CharacterController", "Touched: " + character.toString());

        // Unselected selected character
        if (character.equals(map.getSelected())) {
            map.setSelected(null);
            return false;
        }

        map.setSelected(character);

        return false;
    }
}

package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jingyuyao.tactical.model.Character;

public class CharacterController extends InputListener {
    private final Character character;

    CharacterController(Character character) {
        this.character = character;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.log("CharacterController", "Touched: " + character.toString());
        return false;
    }
}

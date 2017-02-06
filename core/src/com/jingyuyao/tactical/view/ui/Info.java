package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;

class Info extends VerticalGroup {

  private final Skin skin;

  Info(Skin skin) {
    this.skin = skin;
  }

  public void display(Character character) {
    clear();
    addActor(new Label(character.getName(), skin));
    addActor(new Label(String.format(Locale.US, "HP: %d", character.getHp()), skin));
  }
}

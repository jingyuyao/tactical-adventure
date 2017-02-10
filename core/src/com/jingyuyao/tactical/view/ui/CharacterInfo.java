package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CharacterInfo extends VerticalGroup {

  private final Skin skin;
  private Character current;

  @Inject
  CharacterInfo(Skin skin) {
    this.skin = skin;
    columnRight();
  }

  @Override
  public void clear() {
    super.clear();
    current = null;
  }

  public void display(Character character) {
    clear();
    addActor(new Label(character.getName(), skin));
    addActor(new Label(String.format(Locale.US, "HP: %d", character.getHp()), skin));
    current = character;
  }

  public void refresh() {
    if (current != null) {
      display(current);
    }
  }
}

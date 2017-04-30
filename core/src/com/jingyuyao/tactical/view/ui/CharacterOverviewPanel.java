package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterOverviewPanel extends TextPanel<Character> {

  private static final String FMT = "%s\nHP: %d";

  @Inject
  CharacterOverviewPanel(
      final LayerManager layerManager,
      final CharacterDetailLayer characterDetailLayer) {
    addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        characterDetailLayer.display(getObject());
        layerManager.open(characterDetailLayer);
      }
    });
  }

  @Override
  String createText(Character character) {
    return String.format(Locale.US, FMT, character.getName(), character.getHp());
  }
}

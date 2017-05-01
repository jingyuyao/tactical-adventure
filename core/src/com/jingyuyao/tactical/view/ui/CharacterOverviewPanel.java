package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterOverviewPanel extends ButtonPanel<Character> {

  private static final String FMT = "Name: %s\nHP: %d";

  private final LayerManager layerManager;
  private final CharacterDetailLayer characterDetailLayer;

  @Inject
  CharacterOverviewPanel(LayerManager layerManager, CharacterDetailLayer characterDetailLayer) {
    super(Align.right);
    this.layerManager = layerManager;
    this.characterDetailLayer = characterDetailLayer;
  }

  @Override
  String createText(Character character) {
    return String.format(Locale.US, FMT, character.getName(), character.getHp());
  }

  @Override
  void click(Character object) {
    characterDetailLayer.display(object);
    layerManager.open(characterDetailLayer);
  }
}

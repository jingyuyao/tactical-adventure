package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Character;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterOverviewPanel extends ButtonPanel<Character> {

  private static final String FMT = "Name: %s\nHP: %d";

  private final LayerManager layerManager;
  private final CharacterDetailLayer characterDetailLayer;
  private final MessageLoader messageLoader;

  @Inject
  CharacterOverviewPanel(
      LayerManager layerManager,
      CharacterDetailLayer characterDetailLayer,
      MessageLoader messageLoader) {
    super(Align.right);
    this.layerManager = layerManager;
    this.characterDetailLayer = characterDetailLayer;
    this.messageLoader = messageLoader;
  }

  @Override
  Optional<String> createText(Character character) {
    if (character.getHp() <= 0) {
      return Optional.absent();
    }
    return Optional.of(String
        .format(Locale.US, FMT, messageLoader.get(character.getName()), character.getHp()));
  }

  @Override
  void click(Character character) {
    characterDetailLayer.display(character);
    layerManager.open(characterDetailLayer);
  }
}

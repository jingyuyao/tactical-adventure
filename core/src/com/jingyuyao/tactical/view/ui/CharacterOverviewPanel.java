package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.i18n.Message;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterOverviewPanel extends ButtonPanel<Character> {

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
    String name = messageLoader.get(character.getName());
    int hp = character.getHp();
    Message message = UIBundle.OVERVIEW_PANEL.format(name, hp);
    return Optional.of(messageLoader.get(message));
  }

  @Override
  void click(Character character) {
    characterDetailLayer.display(character);
    layerManager.open(characterDetailLayer);
  }
}

package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.i18n.Message;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterOverviewPanel extends ButtonPanel<Ship> {

  private final CharacterDetailLayer characterDetailLayer;
  private final MessageLoader messageLoader;

  @Inject
  CharacterOverviewPanel(
      CharacterDetailLayer characterDetailLayer,
      MessageLoader messageLoader) {
    super(Align.right);
    this.characterDetailLayer = characterDetailLayer;
    this.messageLoader = messageLoader;
  }

  @Override
  Optional<String> createText(Ship ship) {
    if (ship.getHp() <= 0) {
      return Optional.absent();
    }
    String name = messageLoader.get(ship.getName());
    int hp = ship.getHp();
    Message message = UIBundle.OVERVIEW_PANEL.format(name, hp);
    return Optional.of(messageLoader.get(message));
  }

  @Override
  void click(Ship ship) {
    characterDetailLayer.display(ship);
  }
}

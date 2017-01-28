package com.jingyuyao.tactical.data;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.BasePlayer;
import com.jingyuyao.tactical.model.character.CharacterFactory;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Item;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class PlayerLoader {

  private final CharacterFactory characterFactory;
  private final ItemLoader itemLoader;

  @Inject
  PlayerLoader(CharacterFactory characterFactory, ItemLoader itemLoader) {
    this.characterFactory = characterFactory;
    this.itemLoader = itemLoader;
  }

  Iterable<Player> createPlayers(List<PlayerSave> playerSaves) {
    ImmutableList.Builder<Player> builder = ImmutableList.builder();
    for (PlayerSave playerSave : playerSaves) {
      builder.add(createPlayer(playerSave));
    }
    return builder.build();
  }

  private Player createPlayer(PlayerSave playerSave) {
    String className = playerSave.getClassName();
    List<Item> items = itemLoader.createItems(playerSave.getItems());
    Player player;
    if (BasePlayer.class.getSimpleName().equals(className)) {
      player = characterFactory.createBasePlayer(playerSave.getData(), items);
    } else {
      throw new IllegalArgumentException("Unknown player class name: " + className);
    }
    return player;
  }
}
